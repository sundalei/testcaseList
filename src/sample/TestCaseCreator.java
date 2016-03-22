package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class TestCaseCreator {
	private List<String> list = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		String testDir = "F:/TestData";
		String outputDirPath = "F:/TestResults";
		
		TestCaseCreator creator = new TestCaseCreator();
		
		creator.createXMLFile(testDir, outputDirPath);
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public void getAllInputFiles(String root) {
		File baseDir = new File(root);
		if(!baseDir.isDirectory()) {
			return;
		}
		File[] subFiles = baseDir.listFiles();
		
		for(int i = 0, count = subFiles.length; i < count; i++) {
			File subFile = subFiles[i]; 
			if("input".equals(subFile.getName())) {
				list.add(subFile.getAbsolutePath());
			}
			
			getAllInputFiles(subFile.getAbsolutePath());
		}
	}
	
	public void createXMLFile(String testDir, String outputDirPath) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element testCaseListElement = document.addElement("testCaseList");
		
		Element testDirElement = testCaseListElement.addElement("testDir");
		Element outputDirPathElement = testCaseListElement.addElement("outputDirPath");
		testDirElement.setText(testDir);
		outputDirPathElement.setText(outputDirPath);
		
		testCaseListElement.addComment("mode 0 taxonomy -> xmlss [default]\n" +
	          "       1 xmlss -> taxonomy\n" + 
			  "       2 taxonomy -> xmlss -> taxonomy\n" +
			  "       3 taxonomy file list");
		
		this.getAllInputFiles(testDir);
		for(int i = 0, count = list.size(); i < count; i++) {
			String path = list.get(i);
			createTestCase(testCaseListElement, testDir, path);
		}
		
		FileOutputStream fos = new FileOutputStream("test.xml");
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(fos, format);
		writer.write(document);
		writer.close();
	}
	
	private void createTestCase(Element testCaseListElement, String testDir, String path) {
		Element testCaseElement = testCaseListElement.addElement("testCase");
		
		Element testNameElement = testCaseElement.addElement("testName");
		testNameElement.setText(getTestName(path));
		
		Element inputFilePathElement = testCaseElement.addElement("inputFilePath");
		inputFilePathElement.setText(getInputFilePath(testDir, path));
		
		Element correctFilePathElement = testCaseElement.addElement("correctFilePath");
		correctFilePathElement.setText(getCorrectPath(testDir, path));
		
		Element xmlssLoadConfElement = testCaseElement.addElement("xmlssLoadConf");
		xmlssLoadConfElement.setText(getXmlssLoadConf(testDir, path));
		
		Element xmlssReportConfElement = testCaseElement.addElement("xmlssReportConf");
		xmlssReportConfElement.setText(getXmlssReportConfElement(testDir, path));
		
		Element taxonomyDiffConfElement = testCaseElement.addElement("taxonomyDiffConf");
		taxonomyDiffConfElement.setText(getTaxonomyDiffConf(testDir, path));
		
		Element modeElement = testCaseElement.addElement("mode");
		modeElement.setText(getValue( path));
	}
	
	private String getTestName(String path) {
		File file = new File(path);
		file = file.getParentFile();
		String part1 = file.getName();
		file = file.getParentFile();
		String part2 = file.getName();
		return part2 + "_" + part1;
	}
	
	public String getInputFilePath(String testDir, String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		
		for(int i = 0, count = files.length; i < count; i++) {
			File f = files[i];
			if(f.getName().endsWith(".xsd") || f.getName().endsWith(".xlsx")) {
				String temp = f.getAbsolutePath();
				temp = temp.substring(testDir.length() + 1);
				temp = processTemp(temp);
				return temp;
			}
		}
		return "";
	}
	
	public String getCorrectPath(String testDir, String path) {
		File file = new File(path);
		String path1 = file.getParentFile().getAbsolutePath();
		file = new File(path1, "correct");
		File[] files = file.listFiles();
		if(files == null) {
			return "";
		}
		
		for(int i = 0, count = files.length; i < count; i++) {
			File f = files[i];
			if(f.getName().endsWith(".xsd") || f.getName().endsWith(".xlsx")) {
				String temp = f.getAbsolutePath();
				temp = temp.substring(testDir.length() + 1);
				temp = processTemp(temp);
				return temp;
			}
		}
		return "";
	}
	
	public String getXmlssReportConfElement(String testDir, String path) {
		File file = new File(path);
		String path1 = file.getParentFile().getAbsolutePath();
		file = new File(path1, "conf");
		File[] files = file.listFiles();
		if(files == null) {
			return "";
		}
		
		for(int i = 0, count = files.length; i < count; i++) {
			File f = files[i];
			if(f.getName().equals("XMLSSLoad.conf")) {
				String temp = f.getAbsolutePath();
				temp = temp.substring(testDir.length() + 1);
				temp = processTemp(temp);
				return temp;
			}
		}
		return "";
	}
	
	public String getXmlssLoadConf(String testDir, String path) {
		File file = new File(path);
		String path1 = file.getParentFile().getAbsolutePath();
		file = new File(path1, "conf");
		File[] files = file.listFiles();
		if(files == null) {
			return "";
		}
		
		for(int i = 0, count = files.length; i < count; i++) {
			File f = files[i];
			if(f.getName().equals("XMLSSReport.conf")) {
				String temp = f.getAbsolutePath();
				temp = temp.substring(testDir.length() + 1);
				temp = processTemp(temp);
				return temp;
			}
		}
		return "";
	}
	
	public String getTaxonomyDiffConf(String testDir, String path) {
		File file = new File(path);
		String path1 = file.getParentFile().getAbsolutePath();
		file = new File(path1, "conf");
		File[] files = file.listFiles();
		if(files == null) {
			return "";
		}
		
		for(int i = 0, count = files.length; i < count; i++) {
			File f = files[i];
			if(f.getName().equals("TaxonomyDiff.conf")) {
				String temp = f.getAbsolutePath();
				temp = temp.substring(testDir.length() + 1);
				temp = processTemp(temp);
				return temp;
			}
		}
		return "";
	}
	
	public String getValue(String path) {
		if(path.endsWith(".xsd")) {
			return "0";
		} else {
			return "1";
		}
	}
	
	public String processTemp(String temp) {
		String str = temp.replaceAll("\\\\", "/");
		return str;
	}
}
