package org.javachineseime.ime.common;

public class GbkToBig5 {
	private static final long	serialVersionUID	= -1;

	private String				simpleName			= "";
	private String				complexName			= "";

	public String getComplexName() {
		return complexName;
	}

	public void setComplexName(String bigname) {
		this.complexName = bigname;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String name) {
		this.simpleName = name;
	}
}
