import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class holds the frame and initializes all the displays 
 * @author Michael
 *
 */
public class GUI {
	private JFrame frame;
	//the list of String
//	private JList list;
//	//listModel for the List
//	private DefaultListModel listModel;
	private Map map;
	OSMParser prsr;
	//the menubar
	private JMenuBar menuBar;
	//the view menu
	private JMenu menu;
	//the pixel ratio menuitem
	private JMenuItem menuItem;
	private DataHandler dataHandler;
	
	public GUI(OSMParser prsr){
		this.prsr = prsr;
		dataHandler = prsr.getDataHandler();
		frame = new JFrame("Open Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		map = new Map(prsr.getBounds(), dataHandler);
		map.setPreferredSize(new Dimension(600,750));
		map.setVisible(true);
		map.setFocusable(true); 
		
		frame.add(map,BorderLayout.CENTER);
		
//		listModel = new DefaultListModel();
		ArrayList<String> names = prsr.getNames();
		
		//add the name of ways to the list
		
//		for(Object name : names)
//		{
//	        listModel.addElement((String)name);
//		}
//		list = new JList(listModel);
//		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
//		list.addListSelectionListener(this);
//		list.setVisibleRowCount(5);
//	    JScrollPane listScrollPane = new JScrollPane(list);
//	    
	   
		menuBar = new JMenuBar();
		menu = new JMenu("View");
		menuItem = new JMenuItem("Zoom In");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				map.zoomIn();
				map.repaint();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Zoom Out");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				map.zoomOut();
				map.repaint();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Zoom Reset");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				map.resetZoom();
				map.repaint();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Center Map");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				map.reCenter();
				map.repaint();
			}
		});
		menu.add(menuItem);
		
		
		
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	  //  frame.add(listScrollPane, BorderLayout.WEST);
	    
		frame.pack();
		frame.setPreferredSize(new Dimension(1000,750));
		frame.setVisible(true);
		
		
		map.drawWays(names);
	}

	
}
	

