import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

public class FileAudioServlet extends HttpServlet {
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY hh-mm-ss");
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("post called");
		String filename = "";
		
		for (Part part : req.getParts()) {
			filename = part.getSubmittedFileName();
			System.out.println(filename);
			long fname = Long.parseLong(filename);
			filename = sdf.format(new Date(fname)) + ".wav";
			//System.out.println();
			//System.out.println();
			InputStream in = part.getInputStream();
			OutputStream out = new FileOutputStream(new File("/Volumes/Part1/"+filename));
			IOUtils.copy(in,out);
			in.close();
			out.close();
			//System.out.println(filename);
			CreateTranscript.call("/Volumes/Part1/"+filename);
		}
		
	}

}
