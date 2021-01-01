package preferences;

import java.util.prefs.Preferences;

public class getPrefsSingletan {

	private static Preferences prefs;

	public Preferences getPrefs() {
		if (prefs == null) {
			prefs = Preferences.userRoot().node(this.getClass().getName());
		}
		return prefs;
	}
}
