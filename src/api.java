import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;

public class api {
	static JButton enterBtn;
	static JButton cancelBtn;
	static //static JButton enterBtn;
	//GridLayout grid = new GridLayout(2,4);
    JTextField clientid = new JTextField(15);
    static JTextField clientsecret = new JTextField(15);
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();        
        }
        
        
        
    }
    
    public static void readJsonStream(String url) throws IOException {
    	InputStream in = new URL(url).openStream();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.beginObject();
        while (reader.hasNext()) {
        	String name = reader.nextName();
        	System.out.println(name);
        	switch(name) {
        	case "auctions":
        		reader.beginArray();
        		while (reader.hasNext()) {
        			reader.beginObject();
                	String name2 = reader.nextName();
                	System.out.println(name2);
                	reader.endObject();
        		}
        		reader.endArray();
        		break;
        	case "realms":
        		reader.beginArray();
        		reader.beginObject();
        		while (reader.hasNext()) {
        			
                	String name2 = reader.nextName();
                	System.out.println("key: "+name2);
                	String str = reader.nextString();
                	System.out.println("value: "+str);
                	
        		}
        		//reader.endObject();
        		//reader.endArray();
        		break;
        	}
        }
        reader.endObject();
        reader.close();
    }

    public static void getapikey() throws URISyntaxException, IOException, ParseException {
    	final URI uri = new URI("https://develop.battle.net/access/");
		File apikeyfile = new File("apikey.txt");
		if(apikeyfile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(apikeyfile)); 
			String line = br.readLine();
			if (line == null) {
				System.out.println("null apikey file");
			}
			else {
				Master.apikey = line;
			}
			br.close();
			//Master.init();
		}
		if(Master.apikey == null || Master.apikey.isEmpty() || Master.apikey.length()==0){
			class OpenUrlAction implements ActionListener {
			      @Override public void actionPerformed(ActionEvent e) {
			        open(uri);
			      }
			    }
	        JFrame frame = new JFrame("Blizzard API");
	        JPanel panel1 = new JPanel();//new FlowLayout());
	        JPanel panel2 = new JPanel();//new FlowLayout());
	        JPanel panel3 = new JPanel();
	       // JPanel panel2 = new JPanel();//new FlowLayout());
	        frame.getContentPane().setLayout(
	        	    new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
	        	);
	        JLabel prompt1 = new JLabel("        Enter Client ID");

	        panel1.add(prompt1);
	        panel1.add(clientid);
	        JLabel prompt2 = new JLabel("Enter Client Secret");
	        
	        panel2.add(prompt2);
	        panel2.add(clientsecret);
	        
	       // JLabel url = new JLabel("Enter Client____ ID ");
	        
	        JButton urlButton = new JButton("test");
	        panel3.add(urlButton,BorderLayout.WEST);
	        urlButton.setText("<HTML><FONT color=\"#000099\"><U>https://develop.battle.net/access</U></FONT></HTML>");
	        //button.setHorizontalAlignment(SwingConstants.LEFT);
	        //button.setBounds(0, 0, 1000, 0);
	        urlButton.setBorderPainted(false);
	        urlButton.setOpaque(false);
	        urlButton.setBackground(Color.WHITE);
	        urlButton.setToolTipText(uri.toString());
	        urlButton.addActionListener(new OpenUrlAction());
	        //frame.add(outer,BorderLayout.SOUTH);
	        frame.add(panel3);
	        frame.add(panel1);
	        frame.add(panel2);
	        enterBtn = new JButton("Enter");
	        enterBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(clientid.getText()+":"+clientsecret.getText());
					Master.apikey=clientid.getText()+":"+clientsecret.getText();
					try {
						PrintWriter out = new PrintWriter(apikeyfile);
						out.print(Master.apikey);
						out.close();
						frame.dispose();
						Master.init();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					
				}
	        	
	        });
	        cancelBtn = new JButton("Cancel");
	        cancelBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					
				}
	        	
	        });
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(cancelBtn,BorderLayout.EAST);
	        buttonPanel.add(enterBtn,BorderLayout.WEST);
	        frame.add(buttonPanel);
	        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        
		}
		else {
			Master.IDNamesFileIN();
			Master.two();
			
		}
    }
    
    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().browse(uri);
          } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
      }
    
    
	public static String get_token(String userpass) {
	    String basicAuth = new String(new Base64().encode(userpass.getBytes()));
	    CloseableHttpResponse response = null;
	    try {
	        CloseableHttpClient client = HttpClients.createDefault();
	        URI auth = new URIBuilder()
	                .setScheme("https")
	                .setHost("us.battle.net")
	                .setPath("/oauth/token")
	                .setParameter("grant_type", "client_credentials")
	                .build();
	
	        HttpPost post = new HttpPost(auth);
	        Header header = new BasicHeader("Authorization", "Basic " + basicAuth);
	        post.setHeader(header);
	        try {
	            response = client.execute(post);
	            //response.getEntity().getContent()
	            @SuppressWarnings("resource")
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
	            String result = s.hasNext() ? s.next() : "";
	            result = result.substring(result.indexOf(":")+2,result.indexOf("\"",result.indexOf(":")+2));
	            s.close();
	            
	            return result;
	            
	        }
	        finally {
	            response.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return "";
	}
    

}