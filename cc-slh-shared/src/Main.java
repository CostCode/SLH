import java.util.regex.Pattern;


public class Main {

	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile("^[0-9]+|[0-9]+(\\.[0-9]{1,2})$");
		
		boolean result = pattern.matcher("000.1").matches();
		
		System.out.println("OK. result: " + result);
		
	}

}
