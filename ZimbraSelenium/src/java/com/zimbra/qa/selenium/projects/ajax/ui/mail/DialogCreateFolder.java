/**
 * 
 */
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.net.URL;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;

/**
 * Represents a "Create New Folder" dialog box
 * 
 * Lots of methods not yet implemented.  See https://bugzilla.zimbra.com/show_bug.cgi?id=55923
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogCreateFolder extends AbsDialog {

	public static class Locators {
	
	   public static final String zDialogId = "css=div[class*='DwtDialog WindowOuterContainer']";

      // Textfields
      public static final String zNameField = "css=input[id$='_name'][class*='Field']";

      // Buttons
      public static final String zOkButton = "css=tr>td>div[id*='button2']";
      public static final String zCancelButton = "css=tr>td>div[id*='button1']";
	}
	
	
	public DialogCreateFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		logger.info("new "+ DialogCreateFolder.class.getCanonicalName());

	}
	

	/* (non-Javadoc)
	 * @see framework.ui.AbsDialog#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsVisible()");

		String locator = Locators.zNameField;
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false); // Not even present
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);	// Not visible per position
		}
	
		// Yes, visible
		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
		
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		tracer.trace("Click dialog button "+ button);

		AbsPage page = null;
		String locator = null;
		if ( button == Button.B_OK ) {

			locator = Locators.zOkButton;

			this.zClick(locator);

	      this.zWaitForBusyOverlay();

	      // Wait for the spinner image ONLY for desktop
         ((AppAjaxClient)MyApplication).zPageMail.zWaitForDesktopLoadingSpinner(5000);

         // Check the message queue
	      Stafpostqueue sp = new Stafpostqueue();
	      try {
	         sp.waitForPostqueue();
	      } catch (Exception e) {
	         throw new HarnessException("Getting exception while post queueing: " + e.getMessage());
	      }

	      return (page);

		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.zCancelButton;

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		// Default behavior, click the locator
		//
		
		// Make sure the locator was set
		if ( locator == null ) {
			throw new HarnessException("Button "+ button +" not implemented");
		}
		
		// Make sure the locator exists
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Button "+ button +" locator "+ locator +" not present!");
		}
		
		this.zClick(locator);
		
		this.zWaitForBusyOverlay();

		return (page);
	}


	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText("+ locator +")");
		
		if ( locator == null )
			throw new HarnessException("locator was null");
		
		return (this.sGetText(locator));
	}


	/**
	 * Click on the folder in the dialog tree
	 * @param folder
	 * @throws HarnessException
	 */
	public void zClickTreeFolder(FolderItem folder) throws HarnessException {
		logger.info(myPageName() + " zClickTreeFolder("+ folder +")");
		
		tracer.trace("Click on tree folder with name "+ folder.getName());

		if ( folder == null ) 
			throw new HarnessException("folder must not be null");
		
		String locator = "css=div[id='"+ Locators.zDialogId +"'] td[id='zti__ZmChooseFolderDialog_Mail__"+ folder.getId() +"_textCell']";
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder in tree "+ locator);
		
		this.zClick(locator);
		
		this.zWaitForBusyOverlay();

	}

	/**
	 * Enter text into the move message dialog folder name field
	 * @param folder
	 */
	public void zEnterFolderName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName("+ folder +")");

		tracer.trace("Enter folder name in text box "+ folder);

		if ( folder == null ) 
			throw new HarnessException("folder must not be null");

		String locator = Locators.zNameField;

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("unable to find folder name field "+ locator);

		sType(locator, folder);      
	}

	public enum FolderColor {
		None,
		Blue,
		Cyan,
		Green,
		Purple,
		Red,
		Yellow,
		Pink,
		Grey,
		Orange,
		MoreColors
	}
	
	/**
	 * Set the color pulldown
	 * @param folder
	 */
	public void zEnterFolderColor(FolderColor color) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderColor("+ color +")");
		
		tracer.trace("Enter color "+ color);

		if ( color == null ) 
			throw new HarnessException("folder must not be null");
		
		if ( color == FolderColor.MoreColors )
			throw new HarnessException("'more colors' - implement me!");
		
		throw new HarnessException("implement me!");
		
	}


	public void zClickSubscribeFeed(boolean b) throws HarnessException {
		String locator = "CreateNewFolderDialog_remote";
		
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException(locator + " no present!");
		}
		
		if ( this.sIsChecked(locator) == b ) {
			logger.debug("checkbox status matched.  not doing anything");
			return;
		}
		
		this.sCheck(locator);
		
	}


	public void zEnterFeedURL(URL feed) throws HarnessException {
		String locator = "CreateNewFolderDialog_remoteURLfield";

		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException(locator + " no present!");
		}
		
		this.sType(locator, feed.toString());
		
	}


	


}
