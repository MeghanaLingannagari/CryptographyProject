import java.io.*;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;
public class Decryption {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, FileNotFoundException, IOException { //Creating a KeyGenerator object		
		
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");

	      //Creating a SecureRandom object
	      SecureRandom secRandom = new SecureRandom();

	      //Initializing the KeyGenerator
	      keyGen.init(secRandom);

	      //Creating/Generating a key
	      Key key = keyGen.generateKey();     

	      //Creating a Mac object
	      Mac mac = Mac.getInstance("HmacSHA256");

	      //Initializing the Mac object
	      mac.init(key);
	      
	      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	           
	      kpg.initialize(1024);
	      KeyPair kp = kpg.generateKeyPair();
	          
	      Key pub = kp.getPublic();
	      Key ptv = kp.getPrivate();
	          
	      String here = pub.toString();
	      byte[] pub_key = here.getBytes();
	      
	      List readMsgFile = Files.readAllLines(Paths.get("Transmitted_Data"),StandardCharsets.UTF_8);
	      String data = readMsgFile.toString();
	      
	     String publicKeySender = "30819F300D06092A864886F70D010101050003818D003081890281810097E0DEEA3E0D18A5EBE192E20ED610F839D5531008FC013F8EF7D78096787C5B6962AFE749EBEB922C6159552BF9A48D69AF4182A663614CD69FB69EBE4BD7BCA6882E3F1945D25FDB51AA3FDAF67DEC17DBAB5C4849B06A473CD5F8DBC1515271E1D1F0371BB628EDC9660E1CF2ADDF7A79D804436CEA451BF36F70100063C70203010001";
	 	 String privateKeySender = "30820276020100300D06092A864886F70D0101010500048202603082025C0201000281810097E0DEEA3E0D18A5EBE192E20ED610F839D5531008FC013F8EF7D78096787C5B6962AFE749EBEB922C6159552BF9A48D69AF4182A663614CD69FB69EBE4BD7BCA6882E3F1945D25FDB51AA3FDAF67DEC17DBAB5C4849B06A473CD5F8DBC1515271E1D1F0371BB628EDC9660E1CF2ADDF7A79D804436CEA451BF36F70100063C702030100010281804282BB9C2128630EE64996A56091E6448E2F197D00323BE9B5965BCD7817B37DE49CCF239E7FAE5E090968B19FDC676F7644C58F8D861FEA226336FA35A3C909AAC7F4D996CB7C508F907A26B1D0C4309A1D6B9240DBB7AA444A34CDFC386F0333C78B9C6E2B1DA1BF21F48E93B5D3BC0FA19AF9FFD80AC39D2C4AC36FDB8881024100DB364374D7EF2079F4818E32D70458CF201CB5DDB610FAC046293FACAB9D8D740D441944B70868321A3322E43DA2C58828C8BEF719D42AA10605410E2BEC1597024100B15DD65E1D2F90008E23E7C08DFE944731109417AAF7296D198C634F5AB829AA58F5C89E6CFDAF411AB2E14A4451294BE847A44FD27C2566DC3CD1B9ACB6C951024077208B121152EB443B42B8A31A444B7AEC098E2FD9846AB058B08BE0EB3668ABBB95D961855235459D05531DD3FD03F9D5A81A57309B233B7489206C72240F2B024100839280BB401B504F9921B98175D42C5AC362F0F8BDB2ABE06ADB61B03433F9748C9987413EC8D1811235303ACF83058771BFF37BE34084DE50F22D01F7C58081024009FD346B9700738CFB9007DA1FBA589E881AA649D84C42457B087765B89F720FAFEC463807E64A661F20A98DFCCFFD8DF07CC90E99A24074997D038571FC5EF0";
	 	 String publicKeyReceiver = "30819F300D06092A864886F70D010101050003818D0030818902818100987F97B50E3CBD0B5C6F081DE3CE26DFC88511CD4C5E75CE6E935E9BEB80817DD3C33E1E95D9185E50B77FD6B5C762D0BEB5FBD2589EDB2FC920BB52A6DEF2FA1B4C7D761A34CCD7A1058C32D256A45E1819F6106089EA08F913760C92543F6E4AB22E03BE43CE146133FDE78340D616E373FA542F0A8CEB046053D19CDF15330203010001";
	       
	     String encryptedMsgPadding = "***************************MsgPaddingHere***************************";
	     String AESKeyPadding = "***************************AESPaddingHere***************************";
	     String MACPadding = "***************************MACPaddingHere***************************";
	       
	     int encryptedMsgStartIndex = data.indexOf(encryptedMsgPadding) + encryptedMsgPadding.length();
	     int encryptedMsgEndIndex = data.indexOf(AESKeyPadding);
	     int AESKEYstarts = data.indexOf(AESKeyPadding) + AESKeyPadding.length();      
	     int AESYKEYends = data.indexOf(MACPadding); 
	     int MACstart = data.indexOf(MACPadding)  + MACPadding.length();
	     int MACEndIndex = data.length();
	     
	     String encMsg = data.substring(encryptedMsgStartIndex, encryptedMsgEndIndex);
	     String encAESKey = data.substring(AESKEYstarts, AESYKEYends);
	     String encMAC = data.substring(MACstart, MACEndIndex);
	     String decryptedKey = AESreceiver.decryption(encAESKey, publicKeyReceiver);
	     String decryptedMsg = AESreceiver.decryption(encMsg, decryptedKey); //what are you encrypting , what are you encrypting it with
	         
	     File file = new File("New_Transmitted_Data");
	     if(file.createNewFile()) {
	    	  System.out.println("File was sucessfully created: " + file.getName());
	     }
	     else {
	    	  System.out.println("ERROR: File was not created ") ;
	     }
	      
	     FileWriter fileWriter = new FileWriter("New_Transmitted_Data");
	     fileWriter.write(data);
	     fileWriter.close();
         System.out.println("Successfully wrote to the file.");
         
        BufferedWriter output = null;
 		try 
 		 {
 		     File fileObj = new File("New_Transmitted_Data");
 		     output = new BufferedWriter(new FileWriter(fileObj));
 		     output.write(decryptedMsg);
 		 } 
 		 catch ( IOException e ) 
 		 {
 		 } 
 		 finally 
 		 {
 		    if ( output != null )   
 		    {
 		        try {
 					output.close();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 		    }
 		 }
	}
	      	
}
