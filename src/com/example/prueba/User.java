package com.example.prueba;

import java.util.Locale;

public class User implements Comparable<User> {

	private String login;

	private String url;

	private String html_url;

	private String repos_url;

	private String events_url;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	public String getRepos_url() {
		return repos_url;
	}

	public void setRepos_url(String repos_url) {
		this.repos_url = repos_url;
	}

	public String getEvents_url() {
		return events_url;
	}

	public void setEvents_url(String events_url) {
		this.events_url = events_url;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String login, String url, String html_url, String repos_url,
			String event_url) {
		super();
		this.login = login;
		this.url = url;
		this.html_url = html_url;
		this.repos_url = repos_url;
		this.events_url = event_url;
	}

	@Override
	public int compareTo(User user) {

		return this.getLogin().toLowerCase(Locale.ENGLISH)
				.compareTo(user.getLogin().toLowerCase(Locale.ENGLISH));
	}
}
