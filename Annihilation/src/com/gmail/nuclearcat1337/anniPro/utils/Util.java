package com.gmail.nuclearcat1337.anniPro.utils;

import java.io.File;
import java.io.IOException;

public class Util
{
	public static String shortenString(String string, int characters)
	{
		if(string.length() <= characters)
			return string;
		return string.substring(0, characters);
	}
	
	public static boolean tryCreateFile(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
}
