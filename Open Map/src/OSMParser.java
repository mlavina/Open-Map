import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Sample parser for reading Open Street Map XML format files.
 * Illustrates the use of SAXParser to parse XML.
 *
 * @author E. Stark
 * @date September 20, 2009
 */
class OSMParser {

    /** OSM file from which the input is being taken. */
    private File file;
    private HashMap<String, Node> nodes = new HashMap<String,Node>();
    private HashMap<String,Way> ways = new HashMap<String,Way>();
    private HashMap<String, String> wayNames = new HashMap<String,String>();
    private HashMap<String, Relation> relations = new HashMap<String,Relation>();
    private HashMap<String, String> relationNames = new HashMap<String,String>();
    private boolean endNode = true;
    private boolean endWay = true;
    private boolean endRelation = true;
    private String currentType = "";
    private String prevNodeID = "";
    private String prevWayID = "";
    private String prevRelationID ="";
    private Bounds bounds; 
    private DataHandler dataHandler;

    /**
     * Initialize an OSMParser that takes data from a specified file.
     *
     * @param s The file to read.
     * @throws IOException
     */
    public OSMParser(File f) {
	file = f;
    }

    /**
     * Parse the OSM file underlying this OSMParser.
     */
    public void parse()
	throws IOException, ParserConfigurationException, SAXException {
	SAXParserFactory spf = SAXParserFactory.newInstance();
	spf.setValidating(false);
	SAXParser saxParser = spf.newSAXParser();
	XMLReader xmlReader = saxParser.getXMLReader();
	OSMHandler handler = new OSMHandler();
	xmlReader.setContentHandler(handler);
	InputStream stream = null;
	try {
	    stream = new FileInputStream(file);
	    InputSource source = new InputSource(stream);
	    xmlReader.parse(source);
	} catch(IOException x) {
	    throw x;
  	} finally {
	    if(stream != null)
		stream.close();
	}
    }
    public void printData()
	{
		System.out.println("WayData:" + ways);
	}
    /**
     * get the DataHandler 
     */
    public DataHandler getDataHandler()
    {
    	return dataHandler;
    }
    /**
     * calls the hashmap to get the keys which are the names of the way
     * @return array of string representing all the names
     */
    public ArrayList<String> getNames()
    {
    	return  new ArrayList<String>(wayNames.keySet());
    }
    public Bounds getBounds()
	{
		return bounds;
	}
	

    /**
     * Handler class used by the SAX XML parser.
     * The methods of this class are called back by the parser when
     * XML elements are encountered.
     */
    class OSMHandler extends DefaultHandler {

	/** Current character data. */
	private String cdata;

	/** Attributes of the current element. */
	private Attributes attributes;

	/**
	 * Get the most recently encountered CDATA.
	 */
	public String getCdata() {
	    return cdata;
	}

	/**
	 * Get the attributes of the most recently encountered XML element.
	 */
	public Attributes getAttributes() {
	    return attributes;
	}

	/**
	 * Method called by SAX parser when start of document is encountered.
	 */
	public void startDocument() {
	  //  System.out.println("startDocument");
	}

	/**
	 * Method called by SAX parser when end of document is encountered.
	 */
	public void endDocument() {
	    //System.out.println("endDocument");
		dataHandler = new DataHandler(ways,wayNames, nodes, relations,relationNames);
	}

	/**
	 * Method called by SAX parser when start tag for XML element is
	 * encountered.
	 * qNames nd = node, way = way, member = , relation = relation, tag = tag
	 */
	public void startElement(String namespaceURI, String localName,
				 String qName, Attributes atts) {
		if(atts.getLength() > 0)
		{
		    attributes = atts;
		    if("node".equals(qName))
		    {
		    	endNode = false;
		    	getNodeAttrs(atts);
		    }
		    else if("way".equals(qName))
		    {
		    	endWay = false;
		    	getWayAttrs(atts);
		    }
		    else if("nd".equals(qName))
		    {
		    	getNdAttrs(atts);
		    }
		    else if("tag".equals(qName))
		    {
		    	getTagAttrs(atts);
		    }
		    else if("relation".equals(qName))
		    {
		    	endRelation = false;
		    	getRelationAttrs(atts);
		    }
		    else if("member".equals(qName))
		    {
		    	getMemberAttrs(atts);
		    }
		    else if("bounds".equals(qName))
		    {
		    	getBoundsAttrs(atts);
		    }
		    
		    else if("bound".equals(qName)){
		    	getBoundBoxAttrs(atts);
		    }
		   
		}
	}
	
	private void getBoundBoxAttrs(Attributes atts) {
	
		for(int i=0; i < atts.getLength(); i++) {
			String qName = atts.getQName(i);
			String value = atts.getValue(i);
			if("box".equals(qName))
			{
				String[] pts = value.split(",");
				bounds = new Bounds(new Double(pts[1]),new Double(pts[0]),new Double(pts[3]),new Double(pts[2]));
				break;
			}
			
		
		}
		
	}
	
	private void getBoundsAttrs(Attributes atts) {
		double x1 = 0;
		double x2 = 0;
		double y1 = 0;
		double y2 = 0;
		for(int i=0; i < atts.getLength(); i++) {
			String qName = atts.getQName(i);
			String value = atts.getValue(i);
			if("minlat".equals(qName))
			{
				y1 = new Double(value);
			}
			else if("minlon".equals(qName))
			{
				x1 = new Double(value);
			}
			else if("maxlat".equals(qName))
			{
				y2 = new Double(value);
			}
			else if("maxlon".equals(qName))
			{
				x2 = new Double(value);
			}
		
		}
		bounds = new Bounds(x1,y1,x2,y2);
	}

	/**
	 * Method called by SAX parser when end tag for XML element is
	 * encountered.  This can occur even if there is no explicit end
	 * tag present in the document.
	 */
	public void endElement(String namespaceURI, String localName,
			       String qName) throws SAXParseException {
		  if("node".equals(qName))
		    {
		    	endNode = true;
		    }
		    else if("way".equals(qName))
		    {
		    	endWay = true;
		    }
		    else if("relation".equals(qName))
		    {
		    	endRelation = true;
		    }
		  
	}

	/**
	 * Method called by SAX parser when character data is encountered.
	 */
	public void characters(char[] ch, int start, int length)
	    throws SAXParseException {
	    // OSM files apparently do not have interesting CDATA.
	    //System.out.println("cdata(" + length + "): '"
	    // 		       + new String(ch, start, length) + "'");
	    cdata = (new String(ch, start, length)).trim();
	}

	/**
	 * Auxiliary method to display the most recently encountered
	 * attributes.
	 */
	private void showAttrs(Attributes atts) {
	    for(int i=0; i < atts.getLength(); i++) {
		String qName = atts.getQName(i);
		String type = atts.getType(i);
		String value = atts.getValue(i);
		
		System.out.println("\t" + qName + "=" + value
				   + "[" + type + "]");
	    }   
		
	}
	
	/**
	 * the only tag that is important is name so this searches out for name and then
	 * sets the name of the current object i.e node,way or relation and adds that to a hashmap
	 */
	private void getTagAttrs(Attributes atts)
	{
		
		boolean isName = false;
		if(!endWay)
		{
			for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				String type = atts.getType(i);
				String value = atts.getValue(i);
				if("k".equals(qName))
				{
					if("name".equals(value))
					{isName = true;}
				}
				if("v".equals(qName) && isName)
				{
					ways.get(prevWayID).setName(value);
					wayNames.put(value, prevWayID);
					
					
				}
			}
		}
		else if(!endNode)
		{
			for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				String type = atts.getType(i);
				String value = atts.getValue(i);
				if("k".equals(qName))
				{
					if("name".equals(value))
					{isName = true;}
				}
				if("v".equals(qName) && isName)
				{
					nodes.get(prevNodeID).setName(value);
					
					
				}
			}
		}
		
		else if(!endRelation)
		{
			for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				String type = atts.getType(i);
				String value = atts.getValue(i);
				if("k".equals(qName))
				{
					if("name".equals(value))
					{isName = true;}
				}
				if("v".equals(qName) && isName)
				{
					relations.get(prevRelationID).setName(value);
					relationNames.put(value, prevRelationID);
					
					
				}
			}
		}
	}
	/**
	 * get the attributes specific to nds
	 * @param atts
	 */
	private void getNdAttrs(Attributes atts)
	{
		String id = "";
		if(!endWay)
		{
			 for(int i=0; i < atts.getLength(); i++) {
					String qName = atts.getQName(i);
					
					String value = atts.getValue(i);
					if("ref".equals(qName))
					{
						ways.get(prevWayID).add(value);
					}		
					
			 } 
		}
	}
	/**
	 * Method to get attributes specific to the Nodes
	 */
	private void getNodeAttrs(Attributes atts)
	{
		 double lat = 0;
		 double lon = 0;
		 String id = "0";
		 for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				
				String value = atts.getValue(i);
				if("id".equals(qName))
				{
					id = value;
				}
				else if("lat".equals(qName))
				{
					lat = new Double(value);
				}
				else if("lon".equals(qName))
				{
					lon = new Double(value);
				}
				
				
		 } 
		 nodes.put(id, new Node(lat,lon,id));
		 prevNodeID = id;
	}
	
	/**
	 * method to get attributes specifc to Way
	 */
	private void getWayAttrs(Attributes atts)
	{
		 for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				String type = atts.getType(i);
				String value = atts.getValue(i);
				if("id".equals(qName))
				{
					ways.put(value, new Way(value));
					prevWayID = value;
					break;
				}
				
				
		 } 
	}
	
	/**
	 * method to get attributes specific to Relations
	 */
	private void getRelationAttrs(Attributes atts)
	{
		 for(int i=0; i < atts.getLength(); i++) {
				String qName = atts.getQName(i);
				String type = atts.getType(i);
				String value = atts.getValue(i);
				if("id".equals(qName))
				{
					relations.put(value, new Relation(value));
					prevRelationID = value;
				}
				
				
				
		 } 
	}
	/**
	 * the attribute method of the member of relations 
	 * @param atts
	 */
	private void getMemberAttrs(Attributes atts)
	{
		String id = "";
		if(!endRelation)
		{
			 for(int i=0; i < atts.getLength(); i++) {
					String qName = atts.getQName(i);
					
					String value = atts.getValue(i);
					if("type".equals(qName))
					{
						currentType = value;
					}
					else if("ref".equals(qName))
					{
						relations.get(prevRelationID).add(value,currentType);
					}		
					
			 } 
		}
	}
	
}

    /**
     * Test driver.  Takes filenames to be parsed as command-line arguments.
     */
    public static void main(String[] args) throws Exception {
	for(int i = 0; i < args.length; i++) {
	    final OSMParser prsr = new OSMParser(new File(args[i]));
	    prsr.parse();
	    SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				GUI g = new GUI(prsr);
				
			}
	    });
	    
	 
	}
    }
}


