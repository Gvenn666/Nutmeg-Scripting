package nutmeg.scripting.core;

public class ExampleMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] source = {
				"#DEFINE output 0",
				"PUSH_LIT 10",
				"PUSH_LIT 20",
				"ADD",
				"STORE_REF output",
				"#LABEL END",
				"HALT"
		};
		
		LowLevelScript test = new LowLevelScript(source);
		System.out.println(test);
		LowLevelScriptProcessor proccesor = new LowLevelScriptProcessor(test, 1);
		
		proccesor.run(1);
		
		System.out.println(proccesor.getVariable("output"));
	}

}
