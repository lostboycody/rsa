//Source Code by Cody Azevedo
//CS4840
//Dr. Carter
//Java implementation of RSA Encryption algorithm
//Compiles and runs 5/17/16


import java.math.BigInteger;
import java.util.Scanner;

public class RSA {

	//Custom primes, d was calculated using xgcd() method
	static long q = 32119, p = 31379, e = 65537, d =  238967153;

	//n and phi(n) calculated and stored
	static long n = p * q;
	static long pn = (p - 1) * (q - 1);

	//Method for converting a java String to Hexadecimal
	public static String toHex(String arg) {
		  return String.format("%08x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
	}
	
	//Extended GCD algorithm, used to calculate private key
	public static long xgcd(long a, long b) {
		
        long x = 0, y = 1, lastx = 1, lasty = 0, temp;
        while (b != 0) {
            long q = a / b;
            long r = a % b;
			
            a = b;
            b = r;
 
            temp = x;
            x = lastx - q * x;
            lastx = temp;
 
            temp = y;
            y = lasty - q * y;
            lasty = temp;
        }
        
        if (lastx % n > 1 && lastx % n < pn ) {
        	d = lastx % n;
        }
        
        return lastx;   
    }

	//Mypowmod method written by Dr. Carter
	//Used for raising to large powers and taking the mod of
	//the result
    static long mypowmod(long x1, long y1, long mod) {

 	   long tmpy, tmpx;
 	   long result;

 	   tmpx = x1 % mod;
 	   tmpy = y1;
 	   result = 1;

 	   while (tmpy >= 1) {
 	      if (tmpy%2 == 1)
 	         result = (tmpx * result) % mod;
 	      tmpy = tmpy / 2;
 	      tmpx = (tmpx * tmpx) % mod;
 	   }
	   
 	   return result;
 	}

    //Main encrypt method
	public static void encrypt(String s){
		
		int blocks = s.length() / 3;
		int rest = s.length() %  3;
		
		//For each block of 3 characters, calculate mypowmod and
		//represent it as blocks of 8 digit Hex numbers
		for(int i=0; i<blocks; i++) {
			long threechars = s.charAt(i*3) * 256 * 256 + s.charAt(i*3+1) *256 + s.charAt(i*3+2);

			long crypt = mypowmod(threechars, e, n);
			
			System.out.printf("%08X", crypt);
			System.out.print(" ");
			
		}
		
		//Encrypt a remaining block of 2 chars
		if(rest == 2) {
			long twochars = s.charAt(s.length()-2) *256*256 + s.charAt(s.length()-1) *256;

			long crypt1 = mypowmod(twochars, e, n);
			
			System.out.printf("%08X", crypt1);
			System.out.print(" ");
		}
		
		//Encrypt a remaining block of 1 char
		if(rest == 1) {
			long onechar = s.charAt(s.length()-1) *256*256;

			long crypt2 = mypowmod(onechar, e, n);
			
			System.out.printf("%08X", crypt2);
			System.out.print(" ");
		}
	}
	
	//Main decrypt method
	public static void decrypt(String crypt){

		//Convert string into integer value
		BigInteger value = new BigInteger(crypt, 16);
		long longcrypt = value.longValue();
		
		//Calculate mypowmod on converted string
		long decrypt = mypowmod(longcrypt, d,  n);
		
		String plainhex = Integer.toHexString((int) decrypt);
		
		StringBuilder output = new StringBuilder();
		
		//For each 2 characters in the input, interpret as ascii character
	    for (int i = 0; i < plainhex.length(); i += 2) {
	    	
	        String str = plainhex.substring(i, i + 2);
	        output.append((char)Integer.parseInt(str, 16));
	    }
	    
	    System.out.print(output);
	}
	
	public static void main(String[] args) {
		
		//Prompt user for encryption or decryption
		System.out.println("Public Key: (" + e + ", " + n + ")");
		System.out.print("Do you want to encrypt or decrypt?: ");
		
		Scanner scanner = new Scanner(System.in);
		String answer = scanner.next();
		
		//Call encryption method if user selected
		if(answer.equals("encrypt")) {
			
			System.out.println("Enter the string you want to encrypt: ");
		
			while(scanner.hasNext()) {
				
				String plain = scanner.next();
				encrypt(plain);
			}
		}
        
		//Call decryption method if user selected
		else if(answer.equals("decrypt")) {

			System.out.println("Enter the hex you want to decrypt: ");
			while(scanner.hasNext()) {
				
				String hex = scanner.next();
				decrypt(hex);
			}
		}
		
		scanner.close();
	}
}
