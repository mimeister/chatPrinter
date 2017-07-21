package de.chatPrinter.enums;

public enum Month {
	JANUARY("Januar", "01"),
	FEBRUARY("Februar", "02"),
	MARCH("März", "03"),
	APRIL("April", "04"),
	MAY("Mai", "05"),
	JUNE("Juni", "06"),
	JULY("Juli", "07"),
	AUGUST("August", "08"),
	SEPTEMBER("September", "09"),
	OCTOBER("Oktober", "10"),
	NOVEMBER("November", "11"),
	DECEMBER("Dezember", "12");
	
	public final String name;
	public final String monthOfYear;
	
	private static final String JAN = "Januar";
	private static final String FEB = "Februar";
	private static final String MAR = "März";
	private static final String APR = "April";
	private static final String MAI = "Mai";
	private static final String JUN = "Juni";
	private static final String JUL = "Juli";
	private static final String AUG = "August";
	private static final String SEP = "September";
	private static final String OCT = "Oktober";
	private static final String NOV = "November";
	private static final String DEC = "Dezember";
	
	Month(String name, String num){
		this.name = name;
		monthOfYear = num;
	}
	
	public static String monthOfYearFromName(String name) {
		switch (name) {
			case JAN:
				return JANUARY.monthOfYear;
			case FEB:
				return FEBRUARY.monthOfYear;
			case MAR:
				return MARCH.monthOfYear;
			case APR:
				return APRIL.monthOfYear;
			case MAI:
				return MAY.monthOfYear;
			case JUN:
				return JUNE.monthOfYear;
			case JUL:
				return JULY.monthOfYear;
			case AUG:
				return AUGUST.monthOfYear;
			case SEP:
				return SEPTEMBER.monthOfYear;
			case OCT:
				return OCTOBER.monthOfYear;
			case NOV:
				return NOVEMBER.monthOfYear;
			case DEC:
				return DECEMBER.monthOfYear;
			default:
					return null;
		}
	}

}
