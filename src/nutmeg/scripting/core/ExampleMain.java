package nutmeg.scripting.core;

public class ExampleMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] source = {
				"RANDOM",
				"SIN",
				"STORE 0",
				"HALT"
		};
		
		LowLevelScript test = new LowLevelScript(source);
		System.out.println(test);
		LowLevelScriptProcessor proccesor = new LowLevelScriptProcessor(test, 1);
		
		proccesor.run(10);
		
		System.out.println(proccesor.getVariable(0));
	}

}
