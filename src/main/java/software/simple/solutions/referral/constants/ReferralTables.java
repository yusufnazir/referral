package software.simple.solutions.referral.constants;

import software.simple.solutions.framework.core.constants.CxodeTables;

public class ReferralTables extends CxodeTables {

	public final class ACTIVITIES_ {

		public static final String NAME = "ACTIVITIES_";

		public final class COLUMNS {
			public static final String PERSON_ID_ = "PERSON_ID_";
			public static final String DATE_OF_ACTIVITY_ = "DATE_OF_ACTIVITY_";
			public static final String ACTIVITY_TYPE_ID_ = "ACTIVITY_TYPE_ID_";
			public static final String MAIN_ACTIVITY_ID_ = "MAIN_ACTIVITY_ID_";
			public static final String REFERRER_PERSON_ID_ = "REFERRER_PERSON_ID_";
			public static final String ACTIVITY_AMOUNT_ = "ACTIVITY_AMOUNT_";
			public static final String ACTIVITY_REWARD_AMOUNT_ = "ACTIVITY_REWARD_AMOUNT_";
			public static final String CUMULATIVE_REWARD_AMOUNT_ = "CUMULATIVE_REWARD_AMOUNT_";
			public static final String USE_REWARD_ = "USE_REWARD_";
			public static final String USED_REWARD_AMOUNT_ = "USED_REWARD_AMOUNT_";
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

	public final class PERSON_REWARDS_ {

		public static final String NAME = "PERSON_REWARDS_";

		public final class COLUMNS {
			public static final String PERSON_ = "PERSON_ID_";
			public static final String CUMULATIVE_REWARD_ = "CUMULATIVE_REWARD_";
		}
	}

	public final class PERSON_FRIENDS_ {

		public static final String NAME = "PERSON_FRIENDS_";

		public final class COLUMNS {
			public static final String PERSON_ID_ = "PERSON_ID_";
			public static final String FRIEND_ID_ = "FRIEND_ID_";
			public static final String START_DATE_ = "START_DATE_";
			public static final String END_DATE_ = "END_DATE_";
			public static final String ACTIVE_ = "ACTIVE_";
			public static final String TOKEN_ = "TOKEN_";
		}
	}

	public final class REFERRERS_ {

		public static final String NAME = "REFERRERS_";

		public final class COLUMNS {
			public static final String PERSON_ = "PERSON_ID_";
			public static final String START_DATE_ = "START_DATE_";
			public static final String END_DATE_ = "END_DATE_";
		}
	}

}
