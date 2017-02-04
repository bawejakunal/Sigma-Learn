import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class CreateTranscriptServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().write("Please wait... Processing...");
		
		String filepath = request.getParameter("filepath");
		
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("e787ada3-a86d-4268-872e-8293444bea2a", "CDkqNaSxs20i");

		RecognizeOptions options = new RecognizeOptions.Builder()
		  .model("en-US_BroadbandModel")
		  .contentType("audio/wav").continuous(true)
		  .maxAlternatives(1).build();

		BaseRecognizeCallback callback = new BaseRecognizeCallback() {
		  @Override
		  public void onTranscription(SpeechResults speechResults) {
			  StringBuilder sb = new StringBuilder();
			  List<Transcript> transcripts = speechResults.getResults();
			  for(Transcript t:transcripts){
				  List<SpeechAlternative> speechAlternatives = t.getAlternatives();
				  for(SpeechAlternative speechAlternative : speechAlternatives){
					  sb.append(speechAlternative.getTranscript());
				  }
			  }
			
		    System.out.println(sb.toString());
		    
		  }

		  @Override
		  public void onDisconnected() {
			
		  }
		};

		try {
		  service.recognizeUsingWebSocket
		    (new FileInputStream(filepath), options, callback);
		}
		catch (FileNotFoundException e) {
		  e.printStackTrace();
		}
		
	}

}
