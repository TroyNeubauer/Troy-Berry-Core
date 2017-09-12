package com.troyberry.util.profiler;

import java.util.*;

import com.troyberry.file.filemanager.*;

public class Profiler implements FileBase {

	public static final int P_OFFSET = 0;
	public static final int F_OFFSET = 1;

	public static final int MAJOR_OFFSET = 2;
	public static final int MINOR_OFFSET = 3;
	public static final int PATCH_OFFSET = 4;

	private final long createTime;
	private List<ProfileData> data;

	private boolean enabled = true;

	private ProfileSelection currentSelectition;

	public Profiler() {
		this.data = new ArrayList<ProfileData>();
		this.createTime = System.nanoTime();
	}

	Profiler(List<ProfileData> datas, long createTime) {
		this.createTime = createTime;
		this.data = datas;
	}

	public void startSelection(String name) {
		if (!enabled) return;
		if (currentSelectition == null) {
			currentSelectition = new ProfileSelection(System.nanoTime(), name);
		} else throw new IllegalStateException("Profile already active! " + currentSelectition + " please call profiler.endSelectition() first!");
	}

	public void startSection(String name) {
		if (!enabled) return;
		data.add(new ProfileSection(name));
	}

	public void endSection(String name) {
		if (!enabled) return;
		for (ProfileData d : data) {
			if (d instanceof ProfileSection && name.equals(d.getName())) {
				ProfileSection dd = (ProfileSection) d;
				if (!dd.isComplete()) {
					dd.end();
				}
			}
		}
	}

	public void endAndStartSection(String toEnd, String toStart) {
		if (!enabled) return;
		endSection(toEnd);
		startSection(toStart);
	}

	public void endSelection() {
		if (!enabled) return;
		if (currentSelectition != null) {
			currentSelectition.setEndTime(System.nanoTime());
			data.add(currentSelectition);
			currentSelectition = null;
		} else throw new IllegalStateException("Profile to end! Call startSetectition first!");
	}

	public void endAndStartSelectition(String name) {
		if (!enabled) return;
		endSelection();
		startSelection(name);
	}

	public ProfileSelection getCurrentSelectition() {
		return currentSelectition;
	}

	public boolean isRunning() {
		return currentSelectition != null;
	}

	public ProfileSelection getMostRecentSelectition() {
		if (data.size() > 0) {
			ProfileSelection mostRecent = null;
			long mostRecentTime = Long.MIN_VALUE;
			for (int i = 1; i < data.size(); i++) {
				if (data instanceof ProfileSelection) {
					ProfileSelection temp = (ProfileSelection) data.get(i);
					if (temp.getStartTime() - mostRecentTime > 0) {
						mostRecentTime = temp.getStartTime();
						mostRecent = temp;
					}
				}
			}

			return mostRecent;
		}
		return null;
	}

	public List<ProfileSection> getSections() {
		List<ProfileSection> list = new ArrayList<ProfileSection>();
		for (ProfileData data : this.data) {
			if (data instanceof ProfileSection) {
				list.add((ProfileSection) data);
			}
		}

		return list;
	}

	public ProfileSelection getLongestTimeTaken() {
		ProfileSelection longest = null;
		long longestTime = 0;
		for (int i = 1; i < data.size(); i++) {
			ProfileData temp = data.get(i);
			if (temp instanceof ProfileSelection) {

				if (longest == null) {
					longest = (ProfileSelection) temp;
					longestTime = longest.getLength();
					continue;
				}
				ProfileSelection selection = (ProfileSelection) data.get(i);
				if (selection.getLength() - longestTime > 0) {
					longestTime = selection.getLength();
					longest = selection;
				}
			}
		}

		return longest;
	}

	public List<ProfileData> getData() {
		return new ArrayList<ProfileData>(data);
	}

	public long createTime() {
		return createTime;
	}

	void add(ProfileData profileData) {
		if (!enabled) return;
		data.add(profileData);
	}

	public long getCreateTime() {
		return createTime;
	}

	public int size() {
		return data.size();
	}

	public void endAllSection() {
		for (ProfileData d : data) {
			if (d instanceof ProfileSection) {
				ProfileSection dd = (ProfileSection) d;
				if (!dd.isComplete()) dd.end();
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
