package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fujitsu.xml.xbrl.util.xml.XMLUtils;
import com.fujitsu.xml.xpathx.XPathAPI;

public class TestCaseLoader {
	private String testDirPath;
	private String outputDirPath;
	
	public List<TestCase> loadTestcases() {
		
		List<TestCase> list = new ArrayList<TestCase>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse("./TestCaseList.xml");
			testDirPath = this.getNodeContent(doc, "/testCaseList/testDir");
			outputDirPath = this.getNodeContent(doc, "/testCaseList/outputDirPath");
			
			NodeList testCases = XPathAPI.selectNodeList(doc, "/testCaseList/testCase");
			for(int i = 0, count = testCases.getLength(); i < count; i++) {
				TestCase testCase = new TestCase();
				Element testCaseElement = (Element)testCases.item(i);
				
				String testName = getNodeContent(testCaseElement, "testName");
				String relativePath = getNodeContent(testCaseElement, "inputFilePath");
				String xmlssLoadConf = getNodeContent(testCaseElement, "xmlssLoadConf");
				String xmlssReportConf = getNodeContent(testCaseElement, "xmlssReportConf");
				String mode = getNodeContent(testCaseElement, "mode");
				
				testCase.setTestName(testName);
				testCase.setInputFilePath(getAbsolutePath(testDirPath, relativePath));
				testCase.setXmlssLoadConf(getAbsolutePath(testDirPath, xmlssLoadConf));
				testCase.setXmlssReportConf(getAbsolutePath(testDirPath, xmlssReportConf));
				testCase.setMode(Integer.valueOf(mode));
				testCase.setOutputFilePath(getAbsolutePath(outputDirPath, relativePath));
				
				list.add(testCase);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return list;
	}
	
	private String getNodeContent(Node root, String xpath) throws TransformerException {

		Node node = XPathAPI.selectSingleNode(root, xpath);
		if (node != null){
			if (node.getNodeType() == Node.ELEMENT_NODE){
				return XMLUtils.getElementContent((Element)node);
			} else {
				return node.getNodeValue();
			}
		}
		return "";
	}
	
	private String getAbsolutePath(String base, String path) {
		return new File(base, path).getAbsolutePath();
	}
}
