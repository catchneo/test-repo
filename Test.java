import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        String data =   "^field DATE\n" +
                        "01/01/2001\n" +
                        "^field NAME\n" +
                        "Dipti\n" +
                        "Roy\n" +
                        "^field ADDRESS\n" +
                        "250, Wildhorse CT, San Jose, CA\n" +
                        "^field OFFICE_ADDR\n" +
                        "100 Great Oaks Blvd\n" +
                        "San Jose\n" +
                        "CA";

        Map<String, String> fieldsToTags = new HashMap<>();
        fieldsToTags.put("DATE", "<PreferredDate>$1</PreferredDate>");
        fieldsToTags.put("NAME", "<Name firstName=$1 lastName=$2/>");
        fieldsToTags.put("ADDRESS", "<Addresses><Address line1=$1{0} line2=$1{1} city=$1{2} state=$1{3}/></Addresses>");
        fieldsToTags.put("OFFICE_ADDR", "<OfficeAddress line1=$1 city=$2 state=$3/>");


        processData(data, fieldsToTags);
    }

    private static void processData(String data, Map<String, String> fieldsToTags) {
        String[] lines = data.split("\n");
        System.out.println(lines.length);
        int lineNo = 0;
        Map<String, String> fieldDataMap = new HashMap<>();

        String prevFieldName = null;
        int startLine = 0, endLine = 0;

        while (lineNo < lines.length) {
            String val = lines[lineNo];
            if (val.startsWith("^field")) {

                if (prevFieldName != null) {
                    endLine = lineNo;
                    String[] fieldLines = Arrays.copyOfRange(lines, startLine, endLine);
                    fieldDataMap.put(prevFieldName, String.join("; ", fieldLines));
                }

                prevFieldName = val.split(" ")[1];
                startLine = lineNo + 1;
            }
            lineNo++;
        }

        // Flush at the end
        if (prevFieldName != null) {
            endLine = lineNo;
            String[] fieldLines = Arrays.copyOfRange(lines, startLine, endLine);
            fieldDataMap.put(prevFieldName, String.join("; ", fieldLines));
        }

        System.out.println(fieldDataMap);
    }
    private static void processData1(String data, Map<String, String> fieldsToTags) {
        String[] lines = data.split("\n");
        System.out.println(lines.length);
        int lineNo = 0;

        while (lineNo < lines.length) {
            String val = lines[lineNo];
            if (val.startsWith("^field")) {
                String fieldName = val.split(" ")[1];
                String xmlContent = fieldsToTags.get(fieldName);
//                System.out.println(xmlContent);
                String replacedXmlContent = xmlContent;

                int index = -1;
                int startLineNo = lineNo;

                // Replace lines
                while((index = xmlContent.indexOf("$", index + 1)) >= 0) {
                    int nextLineNo = xmlContent.charAt(index+1) - '0';
                    char neextNextChar = xmlContent.charAt(index+2);
                    String replaceStr = "$" + nextLineNo;
//                    System.out.println(index + " -- " + nextLineNo);
                    lineNo = startLineNo+nextLineNo;
                    String replaceVal = lines[lineNo];

                    if (neextNextChar == '{') {
                        int splitIndexNo = xmlContent.charAt(index+3) - '0';
                        String result = replaceVal.split(",")[splitIndexNo];
                        replacedXmlContent = replacedXmlContent.replace(replaceStr+"{" + splitIndexNo + "}", result);
                    } else {
                        replacedXmlContent = replacedXmlContent.replace(replaceStr, replaceVal);
                    }
                }
                System.out.println(replacedXmlContent);
            }
            lineNo++;
        }

    }
}





^form "cbo-0101ao0314" -aduon -ati3
^field IMAGE
^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/cbo-0101ao0314.jpg
^field INS_NAME
Prepared For: KS HO
Date: 07/25/2023
^field AGENCY_NAME
Agency Name: Agent Test
Agency Phone Number: (989) 898-9898
	^field COVER_STATE
	^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/cbo-0101ao0314_second.jpg
^field TODAYS_DT
07/25/2023
^global POLICY_HOLDER
KS HO
	^field SIGNATURE
	^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/paul_vandenbosch.bmp
	^field PRESIDENT_NAME                                                                                                                                                           
	Paul VanDenBosch
																^form "cbr-0173ao0314" -aduon
			^field IMAGE_1
			^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/cbr-0173ao0214.jpg
																^form "cbr-0174ca0214" -aduon
	^field IMAGE_1
	^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/pvr-0144ao.jpg
																									^form "cbr-0174ca0214" -aduon
	^field IMAGE_1
	^graph C:\Work\currentWork\nwpc-spi\web\APP-INF\print/bmps/pvr-0138ao.jpg

^form "multi-line-proposal" -aduon
\defineresolve:PageNumber,1.
^define group:SWITCHPAGE       \page1\field$POSITION.\defineresolve:PageNumber,@(@:PageNumber.+1).
^global PAGE_NUMBER
@:PageNumber.
^field DUMMY_HEADER
^field CURRENT_DATE
07/25/2023
^field AGENT_NAME_ADD
Agent Test / AgentTest
82 Holmes Way, Schaumburg, IL 60193-7417
^field SUMMARY_INS_NAME
Insured Name:
^field INSURED_NAME
KS HO
^field INSURED_ADD
7325 Quivira Rd
Shawnee, KS 66216-3590
						^field HO_PREMIUM
			$622.40
																																								^field PC_PREMIUM
	Not Quoted
	^field PA_PREMIUM
	Not Quoted
	^field PX_PREMIUM
	Not Quoted
^field TOTAL_PREMIUM
$622.40
^field COMMENTS

^field DISCLAIMER_TEXT
									^field HO_QUOTE_APP_ID
			AH-00000469
			^field HO_CURRENT_DATE
			07/25/2023
			^field HO_EFF_DT
			08/24/2024
			^field HO_CARRIER_NAME
			Crestbrook Insurance Company
			^field HO_AGENT_NAME_ADD
			Agent Test / AgentTest
			82 Holmes Way, Schaumburg, IL 60193-7417
			^field HO_INSURED_LABEL
			Insured Name:
			^field HO_INSURED_NAME
			KS HO
			^field HO_INSURED_ADD
			7325 Quivira Rd
Shawnee, KS 66216-3590
			^field HO_POLICY_TYPE
										Personal Tenant
										^field HO_PAYMENT_PLAN
				Direct Statement Bill Full Pay
						^field HO_STATE
			KS
			^field HO_PROTECTION_CLASS
			10
			^field HO_TERRITORY
			603
																		^field HO_HEADING
					Discount(s): 
																																		^field HO_DISCOUNT
					Multiple Policy Discount
																																		^field HO_POLICY_DEDUCTIBLE
							$2,500
																				 																			^field TOTAL_HO_PREMIUM
			622.40
												^field HOCOVERAGE_DESC
			Coverage C - Personal Property
			^field HOCOVERAGE_LT
			$158,827
			^field HOCOVERAGE_PREMIUM
							$484.40
						^field DUMMY_SPACE
						^field HOCOVERAGE_DESC
			Coverage D - Loss of Use
			^field HOCOVERAGE_PREMIUM
			Included
						^field HOCOVERAGE_LT
																		Actual Loss Sustained
															^field DUMMY_SPACE
			^field HOCOVERAGE_DESC
			Coverage E - Personal Liability
			^field HOCOVERAGE_LT
							$300,000
										^field HOCOVERAGE_PREMIUM
				Included
						^field DUMMY_SPACE
			^field HOCOVERAGE_DESC
			Coverage F - Medical Payments to Others
			^field HOCOVERAGE_LT
							$10,000
										^field HOCOVERAGE_PREMIUM
				Included
						^field HO_POLICY_OT_COV_HEAD
													^field HO_OTHER_COVERAGE_DESC
				Biological Deterioration Clean Up
								^field HO_OTHER_COVERAGE_LT
				$10,000
									^field HO_OTHER_COVERAGE_PREMIUM
					Included
								^field DUMMY_SPACE
													^field HO_OTHER_COVERAGE_DESC
									Back Up or Overflow of Sewers or Drains Deductible: $5,000
								^field HO_OTHER_COVERAGE_LT
								^field HO_OTHER_COVERAGE_PREMIUM
				Included
				^field DUMMY_SPACE
													^field HO_OTHER_COVERAGE_DESC
				Building Additions and Alterations - Tenant
								^field HO_OTHER_COVERAGE_LT
				$35,883
				^field HO_OTHER_COVERAGE_PREMIUM
				$38.00
								^field DUMMY_SPACE
																																																																							^field HO_OTHER_COVERAGE_DESC
				Green Rebuilding Coverage
				^field HO_OTHER_COVERAGE_LT
				^field HO_OTHER_COVERAGE_PREMIUM
				$25.00
								^field DUMMY_SPACE
										^field HO_OTHER_COVERAGE_DESC
				Identity Theft or Identity Fraud Expenses
								^field HO_OTHER_COVERAGE_LT
				$25,000
									^field HO_OTHER_COVERAGE_PREMIUM
					Included
								^field DUMMY_SPACE
																			^field HO_OTHER_COVERAGE_DESC
				Loss Assessment - Sec I
								^field HO_OTHER_COVERAGE_LT
				$50,000
									^field HO_OTHER_COVERAGE_PREMIUM
					Included
								^field DUMMY_SPACE
																														            				^field HO_OTHER_COVERAGE_DESC
				Personal Injury Exclusion
				^field HO_OTHER_COVERAGE_LT
				^field HO_OTHER_COVERAGE_PREMIUM
				-$5.00
								^field DUMMY_SPACE
																															^field HO_OTHER_COVERAGE_DESC
				Additional Residence Liability - Rented to Others
								^field HO_OTHER_COVERAGE_LT
				$300,000
				^field HO_OTHER_COVERAGE_PREMIUM
				$80.00
								^field DUMMY_SPACE
																									^field HO_OTHER_COVERAGE_DESC
				Loss Assessment - Sec II
								^field HO_OTHER_COVERAGE_LT
				$50,000
									^field HO_OTHER_COVERAGE_PREMIUM
					Included
								^field DUMMY_SPACE
																								^field POLICY_END_HEAD
										^field ENDORSEMENT_DESC
									G8037KS 09/14 Kansas Credit Disclosure and Fair Credit Reporting Act Requirements
											^field ENDORSEMENT_DESC
									P1404KS 09/14 Tenant Policy
											^field ENDORSEMENT_DESC
									G8001 05/20 Signature Page
											^field ENDORSEMENT_DESC
									P8019 01/13 Windstorm or Hail Deductible
											^field ENDORSEMENT_DESC
									P8018 01/13 Additional Residence Rented to Others
											^field ENDORSEMENT_DESC
									P8010 01/13 Identity Theft or Identity Fraud Expense Coverage
											^field ENDORSEMENT_DESC
									P8016 01/13 Green Rebuilding Coverage
											^field ENDORSEMENT_DESC
									P8044 10/13 Personal Injury Exclusion
										^field DISCLAIMER_TEXT
																	^field PAGE_NUMBER

