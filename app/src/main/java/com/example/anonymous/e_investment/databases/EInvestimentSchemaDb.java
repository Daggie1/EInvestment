package com.example.anonymous.e_investment.databases;

public class EInvestimentSchemaDb {
    public static final class MemberTable{
        public static final String NAME="member";

        public static final class Cols{
            public static final String MEMBERID="member_id";
            public static final String USERNAME="username";
            public static final String PHONE="phone";
            public static final String EMAIL="email";
            public static final String PASSWORD="password";
            public static final String PHOTOCURL="photourl";
        }
    }
    public static final class GroupTable{
        public static final String NAME="group";

        public static final class Cols{

            public static final String GROUPNAME="groupname";
            public static final String AMOUNTCONTRIBUTED="amount_contributed";
            public static final String GROUPID="groupid";
            public static final String PHOTOCURL="photourl";
        }
    }
    public static final class ContributionTable{
        public static final String NAME="contribution";

        public static final class Cols{
            public static final String CONTRIBUTIONID="contribution_id";
            public static final String USERID="userid";
            public static final String AMOUNTCONTRIBUTED="amount_contributed";
            public static final String GROUPID="groupid";
        }
    }
    public static final class TransactionTable{
        public static final String NAME="transaction";

        public static final class Cols{
            public static final String TRANSACTIONID="transaction_id";
            public static final String USERID="userid";
            public static final String DATETRANSACTED="date_transacted";
            public static final String AMOUNTCONTRIBUTED="amount_contributed";
            public static final String GROUPID="groupid";
        }
    }
}
