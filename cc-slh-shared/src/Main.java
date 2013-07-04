import java.util.regex.Pattern;


public class Main {

	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile(".{0,5}");
		
		boolean result = pattern.matcher("1asdasdasdad").matches();
		
		System.out.println("OK. result: " + result);
		
	}

}
