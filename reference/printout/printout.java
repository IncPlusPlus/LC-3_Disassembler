import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

public class printout
{
	public static void main(String[] args)
	{
		try
		{
			int first = 48000;
			int b1;
			int b2;
			byte b = (byte)-3;
			//System.out.println(Arrays.toString(bigIntToByteArray(6)));
			//System.out.println(Integer.toBinaryString((first & 0xFFFF) + 0x100).substring(1));
			int index = 0;
			FileInputStream f = new FileInputStream("C:\\Users\\clohertyr\\OneDrive - Wentworth Institute of Technology\\Documents\\Personal Projects\\LC-3 Disassembler\\Example\\example.obj");
			DataInputStream d = new DataInputStream(f);
			while(d.available() >= 1)
			{
				//if(index % 2 ==0)
				//{
				//	b1 =
				//}
				//byte inByte = (byte)d.readUnsignedByte();
				//System.out.print("Reading " + index + ": " + Integer.toBinaryString((inByte & 0xFFFF) + 0x100).substring(1));
				int currentShort = d.readUnsignedShort();
				index++;
				System.out.println(Integer.toBinaryString((currentShort & 0xFFFF) + 0x10000).substring(1));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static byte[] bigIntToByteArray( final int i ) {
		BigInteger bigInt = BigInteger.valueOf(i);
		return bigInt.toByteArray();
	}
	static String getBits(byte b)
	{
		String result = "";
		for(int i = 0; i < 8; i++)
			result += (b & (1 << i)) == 0 ? "0" : "1";
		return result;
	}
}