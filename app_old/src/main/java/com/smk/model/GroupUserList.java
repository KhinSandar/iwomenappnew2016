package com.smk.model;

import java.util.List;

public class GroupUserList {
	private List<GroupUser> groupUsers;
	
	public GroupUserList(List<GroupUser> groupUsers) {
		super();
		this.groupUsers = groupUsers;
	}

	public List<GroupUser> getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(List<GroupUser> groupUsers) {
		this.groupUsers = groupUsers;
	}
	
}
