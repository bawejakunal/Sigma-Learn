import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class CreateTranscript {

	public static void call(String filename) {
		// System.out.println("method called");

		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("e787ada3-a86d-4268-872e-8293444bea2a", "CDkqNaSxs20i");

		RecognizeOptions options = new RecognizeOptions.Builder()
				.model("en-US_BroadbandModel")
				.contentType("audio/wav")
				.smartFormatting(true)
				.continuous(true).maxAlternatives(1).build();

		BaseRecognizeCallback callback = new BaseRecognizeCallback() {
			@Override
			public void onTranscription(SpeechResults speechResults) {
				try {
					StringBuilder sb = new StringBuilder();
					List<Transcript> transcripts = speechResults.getResults();
					for (Transcript t : transcripts) {
						List<SpeechAlternative> speechAlternatives = t.getAlternatives();
						for (SpeechAlternative speechAlternative : speechAlternatives) {
							sb.append(speechAlternative.getTranscript());
							sb.append(".");
						}
					}
					// System.out.println("response:");
					System.out.println(sb.toString());
					StringEntity json = new StringEntity("{ \"text\" : \"" + sb.toString() + "\" }");

					HttpClient httpclient = HttpClients.createDefault();
					HttpPost httppost = new HttpPost("http://15f304cd.ngrok.io/devFest11/getSummary");

					// Request parameters and other properties.
					// List<NameValuePair> params = new
					// ArrayList<NameValuePair>(2);
					// params.add(new BasicNameValuePair("text",
					// sb.toString()));
					httppost.setEntity(json);
					httppost.setHeader("Accept", "application/json");
					httppost.setHeader("Content-type", "application/json");
					// httppost.setEntity(new UrlEncodedFormEntity(params,
					// "UTF-8"));

					// Execute and get the response.
					HttpResponse response = null;
					try {
						System.out.println("sent");
						response = httpclient.execute(httppost);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream instream = null;
						instream = entity.getContent();
						System.out.println("done");
						instream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onDisconnected() {

			}
		};

		try {
			service.recognizeUsingWebSocket(new FileInputStream(filename), options, callback);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
