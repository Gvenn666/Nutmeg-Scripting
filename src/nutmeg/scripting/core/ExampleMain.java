package nutmeg.scripting.core;

public class ExampleMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] source = {
				"#DEFINE output 0",
				"PUSH_LIT 10",
				"STORE_REF output",
				"HALT"
		};
		
		Script test = new Script(source);
		
		ScriptProcessor proccesor = new ScriptProcessor(test, 1);
		
		proccesor.run(10);
		
		System.out.println(proccesor.getVariable("output"));
	}

}
