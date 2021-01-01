package extra_classes;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class genrateUniqueID {

	String ref;
	String uid;

	public genrateUniqueID(String ref) {
		super();
		this.ref = ref;
	}

	public String getUID() {
		this.uid = generateUID(ref);
		return this.uid;
	}

	private String generateUID(String uuid) {
		String uid = "";
		Date date = new Date();
		long l = date.getTime();
		int ref = 0;
		Random r = new Random();
		for (int i = 0; i < uuid.length(); i++) {
			char c = uuid.charAt(r.nextInt(uuid.length()));
			ref = ref * 10 + ((int) c);
		}
		uid = (r.nextInt(1000) + "") + (l + "");
		return uid;
	}

}
