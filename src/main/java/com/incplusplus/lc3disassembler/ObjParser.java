package com.incplusplus.lc3disassembler;

import java.io.*;
import java.util.ArrayList;

public class ObjParser
{
	DataInputStream inputStream;

	public ObjParser(File file) throws FileNotFoundException
	{
		inputStream = new DataInputStream(new FileInputStream(file));
	}

	public ObjParser(String name) throws FileNotFoundException
	{
		this(new File(name));
	}

	public AssemblyProgram getCode()
	{
		String[] lines = readLines();
		AssemblyProgram program = new AssemblyProgram();

		return program;
	}

	private String[] readLines()
	{
		ArrayList<String> lines = new ArrayList<>();
		try
		{
			while(inputStream.available() >= 1)
			{
				lines.add(Integer.toBinaryString((inputStream.readUnsignedShort() & 0xFFFF) + 0x10000).substring(1));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return lines.toArray(new String[0]);
	}
}
