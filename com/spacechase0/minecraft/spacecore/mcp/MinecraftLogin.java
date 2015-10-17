package com.spacechase0.minecraft.spacecore.mcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/* NOTE: This is NOT used when normally run. You have to manually to and edit a file and add some stuff for it to work.
 * I don't steal your session or anything when you run this in 'release' builds. I promise. :P
 * 
 * To use:
 *  Open net.minecraft.client.main.Main.java
 *  In public static void main(...), change the third parameter of:
 *   Session session = new Session((String)argumentacceptingoptionspec9.value(optionset), s4, (String)argumentacceptingoptionspec11.value(optionset), (String)argumentacceptingoptionspec17.value(optionset));
 *  To:
 *   Session session = new Session((String)argumentacceptingoptionspec9.value(optionset), s4, com.spacechase0.minecraft.spacecore.mcp.MinecraftLogin.getAccessToken( new File( "li.txt" ), (String)argumentacceptingoptionspec9.value(optionset) ), (String)argumentacceptingoptionspec17.value(optionset));
 *  Then open jars (assuming MCP, not Gradle) and place 'li.txt' with your email you use to login on the first line, and your password on the second.
 *  Add --username <name> and --uuid <id> (obtained from: https://api.mojang.com/users/profiles/minecraft/<name>) to your launch arguments.
 */

public class MinecraftLogin
{
	public static String getAccessToken( File file, String uuid )
	{
		if(true)return "FML"; // TODO stuff
		BufferedReader buffer = null;
		try
		{
			InputStream stream = new FileInputStream( file );
			InputStreamReader reader = new InputStreamReader( stream );
			buffer = new BufferedReader( reader );
			
			return getAccessToken( buffer.readLine(), buffer.readLine(), uuid );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return "FML";
		}
		finally
		{
			try
			{
				buffer.close();
			}
			catch ( Exception exception )
			{
			}
		}
	}
	
	private static String getAccessToken( String user, String pass, String uuid ) throws Exception
	{
		URLConnection conn = null;
		BufferedReader buff = null;
		try
		{
			String data = String.format( "{\"agent\":{\"name\":\"Minecraft\",\"version\":\"1\"},\"username\":\"%s\",\"password\":\"%s\",\"clientToken\":\"%s\"}", user, pass, uuid );
			
			conn = ( new URL( "https://authserver.mojang.com/authenticate" ) ).openConnection();
			conn.setDoOutput( true );
			conn.setRequestProperty( "Content-type", "application/json" );
			conn.getOutputStream().write( data.getBytes() );
			
			buff = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
			String contents = "";
			while ( buff.ready() ) contents += ( char ) buff.read();
			
			return new Gson().fromJson( contents, JsonObject.class ).get( "accessToken" ).getAsString();
		}
		finally
		{
			if ( buff != null ) buff.close();
		}
	}
}