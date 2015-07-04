package Browser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;

import org.lobobrowser.html.*;
import org.lobobrowser.html.gui.*;
import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;
import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLLinkElement;
import org.xml.sax.InputSource;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import GUI.*;
import java.util.logging.*;
import java.io.*;
/**
 * Minimal rendering example: google.com.
 */
public class HtmlHandler implements Runnable {
	private static boolean run=false;
	private HtmlPanel htmlPanel=new HtmlPanel();
	private Object Parent=null;
	private Thread MyThread=new Thread(this);
	private ArrayList Work=new ArrayList();//A list of web-sites to render.
	
	public HtmlHandler(){
		run=true;
		MyThread=new Thread(this);
		MyThread.start();
	}
	/**
	Return the HtmlPanel view.
	*/
	public HtmlPanel getView(){
		return(htmlPanel);
	}
	
	public synchronized void parseDocument(URL url,final JInternalFrame C){		
		Work.add(new Object[]{url,C});
	}
	
	
	/**
	Parse a document given a String as input.
	*/
	public synchronized void parseDocument(String data,final JInternalFrame C){		
		Work.add(new Object[]{data,C});
	}

	private class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
		// Override methods here to implement browser functionality
		public LocalHtmlRendererContext(HtmlPanel contextComponent) {
			super(contextComponent,new SimpleUserAgentContext());
		}
		
		//A link has been clicked.
		public void navigate(URL href, String target){
			System.out.println("URL CLick.");
			System.out.println("HREF: "+href.toString());
			if(Parent instanceof WebBrowser){
				//System.out.println("Link Clicked in Search--"+href.getPath()+"   "+target);
				WebBrowser WB = (WebBrowser)Parent;
				WB.setLink(href);
			}
			else if(Parent instanceof TutorialWindow){
				TutorialWindow tut = (TutorialWindow)Parent;
				tut.linkGo(href);
			}
			
		}
		
		//A form has been submitted...This is only used in a few special cases.
		public void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs){
			//System.out.println("Form submit?");
			if(Parent instanceof WebBrowser){
				//System.out.println("Browser");
				WebBrowser WB = (WebBrowser)Parent;
				//System.out.println(action.toString());
				String[] link = action.toString().split("/");
				String l="";
				//System.out.println(link.length);
				if(link.length>3){
					
					l=link[3];
				}
				//System.out.println(l);
				if(l.equals("search.html")){
					WB.newSearch(formInputs[0].getTextValue(),"");
				}
				else{
					//System.out.println("Submitting Forms");
					WB.submitForm(formInputs);
				}
			}
		}
		
		public void alert(String message){
			//System.out.println("ALERT CALLED");
		
		}
		
		public boolean confirm(String message){
			//System.out.println("CONFIRM CALLED");
			return false;
		}
		
		public String prompt(String message,String inputDefault){
			//System.out.println("PROMPT CALLED");
			return "";
		}
		
		public void reload(){
			
		}
		
		public void back(){
		
		}
		
		public void onMouseOut(HTMLElement element, MouseEvent event) {
			if(element instanceof HTMLLinkElement){
				//System.out.println("Mouse Off of link");
				if(Parent instanceof JInternalFrame){
					JInternalFrame iFrame = (JInternalFrame)Parent;
					iFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
		
		public void onMouseOver(HTMLElement element, MouseEvent event) {
			if(element instanceof HTMLLinkElement){
				//System.out.println("Mouse Over link");
				if(Parent instanceof JInternalFrame){
					JInternalFrame iFrame = (JInternalFrame)Parent;
					iFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
		}
		
		public boolean onContextMenu(HTMLElement element, MouseEvent event) {
			if(element instanceof HTMLLinkElement){
				int x = event.getX();
				int y = event.getY();
				if(Parent instanceof JInternalFrame){
					JInternalFrame iFrame = (JInternalFrame)Parent;
					JPopupMenu menu = new JPopupMenu();
					JMenuItem menuItem;
					
					menuItem = new JMenuItem("Open");
					menu.add(menuItem);
					menuItem = new JMenuItem("Open in new tab");
					menu.add(menuItem);
					//menu.show(htmlPanel,x,y);
				}
			}
			return true;
		}

	}
	
	//Set the parent associated with this class.
	public void setParent(Object Parent){
		this.Parent=Parent;
	}
	
	

	
	public void run(){
		while(run){
			try{
				Object O[]=null;
				if(Work.size()>0){
					O=(Object[])Work.remove(0);
				}
				if(O!=null){
					if(O[0] instanceof URL){
						URL url=(URL)O[0];
						final JInternalFrame C=(JInternalFrame)O[1];
														
						try{
							String uri="http://www.hackwars.net";
							URLConnection connection = url.openConnection();
							InputStream in = connection.getInputStream();
							
							// A Reader should be created with the correct charset,
							// which may be obtained from the Content-Type header
							// of an HTTP response.
							Reader reader = new InputStreamReader(in);

							// InputSourceImpl constructor with URI recommended
							// so the renderer can resolve page component URLs.
							InputSource is = new InputSourceImpl(reader, uri);
							//htmlPanel = new HtmlPanel();
							HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel);
							
							// Set a preferred width for the HtmlPanel,
							// which will allow getPreferredSize() to
							// be calculated according to block content.
							// We do this here to illustrate the 
							// feature, but is generally not
							// recommended for performance reasons.
							//htmlPanel.setPreferredWidth(800);
							
							// This example does not perform incremental
							// rendering. 
							DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
							Document document = builder.parse(is);
							in.close();

							// Set the document in the HtmlPanel. This
							// is what lets the document render.
							htmlPanel.setDocument(document, rendererContext);
							java.util.Enumeration E=java.util.logging.LogManager.getLogManager().getLoggerNames();
							while(E.hasMoreElements()){
								String o=(String)E.nextElement();
								Logger.getLogger(o).setLevel(Level.OFF);
								//System.out.println("LOGGER ------------------------"+o);
							}
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									C.validate();
									//C.setVisible(true);
								}
							});
						}catch(Exception e){
							e.printStackTrace();
						}	
									
						
					}else{
									
						String data=(String)O[0];
						final JInternalFrame C=(JInternalFrame)O[1];
						
						try{
								
							String uri = "http://hackwars.net";

							// A Reader should be created with the correct charset,
							// which may be obtained from the Content-Type header
							// of an HTTP response.
							Reader reader = new InputStreamReader(new ByteArrayInputStream(data.getBytes()));

							// InputSourceImpl constructor with URI recommended
							// so the renderer can resolve page component URLs.
							InputSource is = new InputSourceImpl(reader, uri);
							//htmlPanel = new HtmlPanel();
							HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel);
							
							// Set a preferred width for the HtmlPanel,
							// which will allow getPreferredSize() to
							// be calculated according to block content.
							// We do this here to illustrate the 
							// feature, but is generally not
							// recommended for performance reasons.
							htmlPanel.setPreferredWidth(800);
							
							// This example does not perform incremental
							// rendering. 
							DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
							Document document = builder.parse(is);

							// Set the document in the HtmlPanel. This
							// is what lets the document render.
							htmlPanel.setDocument(document, rendererContext);
							java.util.Enumeration E=java.util.logging.LogManager.getLogManager().getLoggerNames();
							while(E.hasMoreElements()){
								String o=(String)E.nextElement();
								Logger.getLogger(o).setLevel(Level.OFF);
								//System.out.println("LOGGER ------------------------"+o);
							}
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									C.validate();
									//C.setVisible(true);
								}
							});
							
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}
				}
			
				MyThread.sleep(100);
			}catch(Exception e){
				
			}
		}
	}
}
