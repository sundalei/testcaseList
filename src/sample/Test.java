package sample;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		TestCaseLoader loader = new TestCaseLoader();
		List<TestCase> list = loader.loadTestcases();
		
		for(TestCase testCase: list) {
			String testName = testCase.getTestName();
			String inputFilePath = testCase.getInputFilePath();
			String outputFilePath = testCase.getOutputFilePath();
			
			System.out.println(testName);
			System.out.println(inputFilePath);
			System.out.println(outputFilePath);
			System.out.println("----------------------------------------");
		}
	}
}
