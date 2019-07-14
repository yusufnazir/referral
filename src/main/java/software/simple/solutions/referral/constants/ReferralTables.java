package software.simple.solutions.referral.constants;

import software.simple.solutions.framework.core.constants.CxodeTables;

public class ReferralTables extends CxodeTables {

	public final class ACTIVITIES_ {

		public static final String NAME = "ACTIVITIES_";

		public final class COLUMNS {
			public static final String PERSON_ID_ = "PERSON_ID_";
			public static final String DATE_OF_ACTIVITY_ = "DATE_OF_ACTIVITY_";
			public static final String ACTIVITY_TYPE_ID_ = "ACTIVITY_TYPE_ID_";
			public static final String REFERRER_PERSON_ID_ = "REFERRER_PERSON_ID_";
			public static final String ACTIVITY_AMOUNT_ = "ACTIVITY_AMOUNT_";
			public static final String REFERRER_ACTIVITY_REWARD_ = "REFERRER_ACTIVITY_REWARD_";
			public static final String REFERRER_REWARD_AMOUNT_ = "REFERRER_REWARD_AMOUNT_";
			public static final String ACTIVE_ = "ACTIVE_";
		}
	}

	public final class ACTIVITY_TYPES_ {

		public static final String NAME = "ACTIVITY_TYPES_";

		public final class COLUMNS {
			public static final String CODE_ = "CODE_";
			public static final String NAME_ = "NAME_";
			public static final String DESCRIPTION_ = "DESCRIPTION_";
			public static final String ACTIVE_ = "ACTIVE_";
		}
	}

}
