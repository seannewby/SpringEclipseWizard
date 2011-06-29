/**
 * 
 */
package com.seannewby.eclipse.spring.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;



public class New286SpringMVCPortletProjectWizard extends com.ibm.etools.portlet.wizard.internal.newcomp.PortletComponentCreationWizard {

	/*
	 * Use the WizardNewProjectCreationPage, which is provided by the Eclipse
	 * framework.
	 */
	private WizardNewProjectCreationPage wizardPage;

	private IConfigurationElement config;

	private IWorkbench workbench;

	private IStructuredSelection selection;

	private IProject project;


	
	public boolean performFinish(){
		
		boolean result =  super.performFinish();
		
		
		System.out.println("HELLO");
		
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject((String)this.model.getProperty("IFacetDataModelProperties.FACET_PROJECT_NAME"));
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		final IProjectDescription desc = workspace.newProjectDescription(project.getName());
		
		
		/*
		 * Just like the NewFileWizard, but this time with an operation object
		 * that modifies workspaces.
		 */
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				modifyProject(desc, project, monitor);
			}
		};
		
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		
		return result; 
		
	}
	
	void modifyProject(IProjectDescription description, IProject proj,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {
			System.out.println("Entered..........");
			final String newline = System.getProperty("line.separator");
			String portletName = "";
			String portletPackage = "";
			String portletClassName = "";
			String portletPackageAsFilePath = "";

			proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1000));
			
			IContainer container = (IContainer) proj;
			
			
			//Start Modify portlet.xml
			IFile portletXML = container.getFile(new Path("WebContent/WEB-INF/portlet.xml"));
			InputStream is = portletXML.getContents();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
            String line;
            StringBuilder sb = new StringBuilder();
            try{
                while ((line = reader.readLine()) != null) {
                    if(line.indexOf("portlet-class") > -1){
                    	portletPackage = line.replaceFirst("<portlet-class>", "");
                    	portletPackage = portletPackage.replaceFirst("</portlet-class>", "");
                    	
                    	line = "		<portlet-class>org.springframework.web.portlet.DispatcherPortlet</portlet-class>";
                    }
                    if(line.indexOf("portlet-name") > -1){
                    	portletName = line.replaceFirst("<portlet-name>", "");
                    	portletName = portletName.replaceFirst("</portlet-name>", "");
                    	
                    }
                    sb.append(line);
                    sb.append(newline);
                }
            	
            }
            finally{
            	reader.close();
            }
            is = new ByteArrayInputStream(sb.toString().getBytes());
            portletXML.setContents(is, true, true, monitor);            
            System.out.println(sb.toString());
            System.out.println("Porlet Name: " + portletName);
            System.out.println("Porlet Package: " + portletPackage);
            //Finish Modify portlet.xml
            
            
            //Start set portlet values
            System.out.println("/n/n/n");
            int lastDot = portletPackage.lastIndexOf(".");
            portletClassName = portletPackage.substring(lastDot + 1, portletPackage.length());
            portletClassName = portletClassName.trim();
            System.out.println("portletClassName: " + portletClassName);
            portletPackage = portletPackage.substring(0, lastDot);
            portletPackage = portletPackage.trim();
            System.out.println("portletPackage: " + portletPackage);
            portletName = portletName.trim();
            System.out.println("portletName: " + portletName);            
            portletPackageAsFilePath = portletPackage.replace(".", "/");
            System.out.println("portletPackageAsFilePath: " + portletPackageAsFilePath);
          	//End set portlet values
            
            //Start modify web.xml
            sb = new StringBuilder();
			IFile webXML = container.getFile(new Path("WebContent/WEB-INF/web.xml"));
			is = webXML.getContents();
            reader = new BufferedReader(new InputStreamReader(is));	
            
			InputStream isTemplate = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("templates/web-xml-fragment-template.resource");
			BufferedReader readerTemplate = new BufferedReader(new InputStreamReader(isTemplate));
	
            try{
                while ((line = reader.readLine()) != null) {
                    if(line.indexOf("</display-name>") > -1){
                    	
                        sb.append(line);
                        sb.append(newline);
                        
                        try{
                        	while ((line = readerTemplate.readLine()) != null) {
                                sb.append(line);
                                sb.append(newline);                        		                       		
                        	}
                        }
                        finally{
                        	readerTemplate.close();
                        }
                    
                    	
                    }
                    else{
                        sb.append(line);
                        sb.append(newline);                    	
                    }
                }
            	
            }
            finally{
            	reader.close();
            }
            is = new ByteArrayInputStream(sb.toString().getBytes());
            webXML.setContents(is, true, true, monitor);
            //End modify web.xml
            
            
            //Start move spring jars
            IFile springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.asm.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.asm.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.beans.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.beans.jar");
            springJar.create(is, true, monitor);      
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.context.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.context.jar");
            springJar.create(is, true, monitor); 
 
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.context.support.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.context.support.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.core.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.core.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.expression.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.expression.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.web.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.web.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.web.portlet.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.web.portlet.jar");
            springJar.create(is, true, monitor);
            
            springJar = container.getFile(new Path("WebContent/WEB-INF/lib/org.springframework.web.servlet.jar"));
            is = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("jars/org.springframework.web.servlet.jar");
            springJar.create(is, true, monitor);
            //End move spring jars
            
            
            //Start index jsp
            sb = new StringBuilder();
			final IFolder jspFolder = container.getFolder(new Path("WebContent/WEB-INF/jsp"));
			jspFolder.create(true, true, monitor);
			
			final IFile jsp = container.getFile(new Path("WebContent/WEB-INF/jsp/index.jsp"));
			
			isTemplate = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("templates/index-jsp-template.resource");
			readerTemplate = new BufferedReader(new InputStreamReader(isTemplate));
			
            try{
                while ((line = readerTemplate.readLine()) != null) {

                    sb.append(line);
                    sb.append(newline);
                }
            	
            }
            finally{
            	reader.close();
            }		
            is = new ByteArrayInputStream(sb.toString().getBytes());
            jsp.create(is, true, monitor);
            //End index jsp
			
			
			
            
            //Start context.xml
            sb = new StringBuilder();
			final IFolder contextFolder = container.getFolder(new Path("WebContent/WEB-INF/context"));
			contextFolder.create(true, true, monitor);
			
			final IFile contextXml = container.getFile(new Path("WebContent/WEB-INF/context/applicationContext.xml"));
			
			isTemplate = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("templates/application-context-template.resource");
			readerTemplate = new BufferedReader(new InputStreamReader(isTemplate));
			
            try{
                while ((line = readerTemplate.readLine()) != null) {
                	line = line.replaceAll("\\$\\{myPortletPackage\\}", portletPackage);
                	line = line.replaceAll("\\$\\{myPortletClassName\\}", portletClassName);
                    sb.append(line);
                    sb.append(newline);
                }
            	
            }
            finally{
            	reader.close();
            }		
            is = new ByteArrayInputStream(sb.toString().getBytes());
            contextXml.create(is, true, monitor);			
			//End context.xml
            
            
            
            //Start Controller
            sb = new StringBuilder();
			final IFolder controllerFolder = container.getFolder(new Path("src/" + portletPackageAsFilePath + "/controllers"));
			controllerFolder.create(true, true, monitor);
			
			final IFile controller = container.getFile(new Path("src/" + portletPackageAsFilePath + "/controllers/IndexController.java"));
			
			isTemplate = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("templates/controller-template.resource");
			readerTemplate = new BufferedReader(new InputStreamReader(isTemplate));
			
            try{
                while ((line = readerTemplate.readLine()) != null) {
                	line = line.replaceAll("\\$\\{myControllerPackage\\}", portletPackage + ".controllers");
                    sb.append(line);
                    sb.append(newline);
                }
            	
            }
            finally{
            	reader.close();
            }		
            is = new ByteArrayInputStream(sb.toString().getBytes());
            controller.create(is, true, monitor);	
            //End Controller
            
            
            //Start portlet context
            sb = new StringBuilder();
			
			final IFile portletContext = container.getFile(new Path("WebContent/WEB-INF/" + portletName + "-portlet.xml"));
			
			isTemplate = New286SpringMVCPortletProjectWizard.class.getResourceAsStream("templates/portlet-context-template.resource");
			readerTemplate = new BufferedReader(new InputStreamReader(isTemplate));
			
            try{
                while ((line = readerTemplate.readLine()) != null) {
                	line = line.replaceAll("\\$\\{myControllerPackage\\}", portletPackage + ".controllers");
                    sb.append(line);
                    sb.append(newline);
                }
            	
            }
            finally{
            	reader.close();
            }		
            is = new ByteArrayInputStream(sb.toString().getBytes());
            portletContext.create(is, true, monitor);	            
            //End portlet context
            
            //Start remove original IBM portlet class
            final IFile ibmPortletClass = container.getFile(new Path("src/" + portletPackageAsFilePath + "/" + portletClassName + ".java"));
            ibmPortletClass.delete(true, monitor);
            
            
            //End remove original IBM portlet class
            

		} catch (Exception ioe) {
			IStatus status = new Status(IStatus.ERROR, "New286SpringMVCPortletProjectWizard", IStatus.OK,
					ioe.getLocalizedMessage(), null);
			throw new CoreException(status);
		} finally {
			monitor.done();
		}
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.selection = selection;
		this.workbench = workbench;
	}
	





}
