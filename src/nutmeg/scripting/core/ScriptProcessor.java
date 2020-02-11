package nutmeg.scripting.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import nutmeg.scripting.api.ScriptAPI;

public class ScriptProcessor implements ScriptAPI{
	private Script script;
	private Stack<Integer> stack, callStack;
	private int[] heap = new int[256];
	private volatile HashMap<String, Integer> varMap;
	
	
	private int
	PC = 0x0000,
	datPtr = 0x0000;
	
	public ScriptProcessor(Script _script, int heapSizeKW) {
		stack = new Stack<Integer>();
		callStack = new Stack<Integer>();
		script = _script;
		heap = new int[heapSizeKW * 1000];
		varMap = new HashMap<String, Integer>();
		for(int i = 0; i < script.size(); i++) {
			String line = script.getLineAt(i);
			line = line.trim();
			String[] parts = line.split("\\s");
			int a, b, c, d;
			String s0, s1, s2;
			switch(parts[0].toUpperCase()) {
			case "#DEFINE":
				s0 = parts[1];
				s1 = parts[2];
				
				varMap.put(s0, Integer.parseInt(s1));
				break;
			case "#LABEL":
				s0 = parts[1];
				varMap.put(s0, i);
				break;
			}
		}
		
	}
	
	public Thread runAsync(int frequency) {
		Thread t = new Thread(() -> {
			run(frequency);
		});
		t.start();
		return t;
	}
	
	private boolean
	negativeFlag, zeroFlag;
	
	public int clock() {
		String line = script.getLineAt(PC++);
		line = line.trim();
		String[] parts = line.split("\\s");
		int a, b, c, d;
		String s0;
		switch(parts[0].toUpperCase()) {
			case "HALT": return 1;
			case "PUSH_LIT": stack.push(Integer.parseInt(parts[1])); break; 
			case "PUSH_REF": if(varMap.containsKey(parts[1])) stack.push(heap[varMap.get(parts[1])]); break;
			case "POP": stack.pop(); break;
			
			case "SYS_REDIR_STDOUT": try { System.setOut(new PrintStream(new File(parts[1]))); } catch (FileNotFoundException e) {} break;
			
			case "SYS_REDIR_STDIN": try { System.setIn(new FileInputStream(new File(parts[1]))); } catch (FileNotFoundException e) {} break;
			
			case "SYS_PRINTLN": while(stack.peek() > 0) System.out.println((char) stack.pop().intValue()); break;
			case "SYS_READLN": s0 = stdin.nextLine(); stack.push(0); for(int i = s0.length(); i > 0; i--) { stack.push((int) s0.toCharArray()[i]); } break;
			
			case "SYS_READ_RAW_INTS": varMap.put(parts[1], datPtr); loadRawData(new File(parts[2]));
			
			case "ADD": a = stack.pop(); b = stack.pop(); stack.push(a + b); break;
			case "SUBTRACT": a = stack.pop(); b = stack.pop(); stack.push(a - b); break;
			case "DIVIDE": a = stack.pop(); b = stack.pop(); stack.push(a / b); break;
			case "MULTIPLY": a = stack.pop(); b = stack.pop(); stack.push(a + b); break;
			case "SIGN": a = stack.pop(); b = stack.pop(); stack.push(a + b); break;
			
			case "SIN": a = stack.pop(); stack.push((int) Math.sin(a) * Integer.MAX_VALUE); break;
			case "COS": a = stack.pop(); stack.push((int) Math.cos(a) * Integer.MAX_VALUE); break;
			case "TAN": a = stack.pop(); stack.push((int) Math.tan(a) * Integer.MAX_VALUE); break;
			
			case "RANDOM": stack.push((int) Math.random() * Integer.MAX_VALUE); break;
			
			case "DUPLICATE": stack.push(stack.peek()); break;
			
			case "JUMP": PC = stack.pop(); break;
			case "JUMP_LITERAL": PC = Integer.parseInt(parts[1]); break;
			case "JUMP_IF_ZERO": if(zeroFlag) PC = Integer.parseInt(parts[1]); break;
			case "JUMP_IF_NEGATIVE": if(negativeFlag) PC = Integer.parseInt(parts[1]); break;
			case "JUMP_IF_NOT_ZERO": if(!zeroFlag) PC = Integer.parseInt(parts[1]); break;
			case "JUMP_IF_NOT_NEGATIVE": if(!negativeFlag) PC = Integer.parseInt(parts[1]); break;
			
			case "JUMP_SUBROUTINE": callStack.push(PC); PC = Integer.parseInt(parts[1]); break;
			case "RETURN": PC = callStack.pop(); break;
			
			case "LOAD": stack.push(heap[Integer.parseInt(parts[1])]); break;
			case "STORE": heap[Integer.parseInt(parts[1])] = stack.pop(); break;
			
			case "STORE_REF": heap[varMap.get(parts[1])] = stack.pop(); break;
			
		}
		if(!stack.isEmpty()) {
//		negativeFlag = (stack.peek() < 0);
//		zeroFlag = (stack.peek() == 0);
		}
		
		return 0;
		
	}
	
	Scanner stdin = new Scanner(System.in);
	
	public void run(int frequency) {
		while(!script.isOutOfRange(PC)) {
			try {  Thread.sleep((1 / frequency) * 1000); } catch (InterruptedException e) { e.printStackTrace(); }
  			if(clock() > 0) return;
		}
	}

	@Override
	public int getVariable(String name) {
		if(varMap.containsKey(name)) {
			return varMap.get(name);
		} else {
			return -1;
		}
	}
	
	public int getVariable(int index) {
		return heap[index];
	}
	
	private void loadRawData(File file) {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(file));
			if(file.length() > (heap.length - datPtr)) throw new RuntimeException("File'"+file.getName()+"' cannot be loaded into Heap, "+file.length()+"B");
			for(int i = 0; i < file.length() / Integer.BYTES; i++) {
				heap[datPtr++] = input.readInt();
			}
		} catch(IOException ex) {
			
		}
	}
	
	
	
	
}
