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









public List<TagInfo> deriveFormTagsAndValues(String mergeData, String formTemplateIdRef) throws IBIZException, ServiceHandlerException {
		ModelBean errors = null;
		//ModelBean application = null;
		SAXBuilder builder = null;
		Document doc = null;
		String finalString = null;
		Map<String, String> fieldDataMap = null;
		List<TagInfo> tagInfoList = new ArrayList<>();

		try {
			Log.debug("Processing CorrespondenceXMLCreationHandler....");

			MDATemplate formTemplateMDA = (MDATemplate) Store.getModelObject("form-template", "FormTemplate", formTemplateIdRef);
			ModelBean formTemplate = formTemplateMDA.getBean();
			String responseTemplateMDA = formTemplate.gets("ResponseTemplateMDA");

			//Convert output to Key-Value pairs
			String[] lines = mergeData.split("\n");
			//System.out.println(lines.length);
			int lineNo = 0;
			fieldDataMap = new HashMap<>();

			String prevFieldName = null;
			int startLine = 0, endLine = 0;

			while (lineNo < lines.length) {
				String val = lines[lineNo].trim().replaceAll("\t", "");
				if (val.startsWith("^field") || val.startsWith("^global") || val.startsWith("^graph")) {
					if (prevFieldName != null) {
						endLine = lineNo;
						String[] fieldLines = Arrays.copyOfRange(lines, startLine, endLine);

						TagInfo tagInfo = new TagInfo();
						tagInfo.setTagKey(prevFieldName.replaceAll("\\r",""));
						tagInfo.setTagValue(Arrays.stream(fieldLines).map(v -> v.replaceAll("\\t","").replaceAll("\\r","").trim()).collect(Collectors.joining(",")));
						tagInfoList.add(tagInfo);
					}

					prevFieldName = val.split(" ")[1];
					startLine = lineNo + 1;
				}
				else if(val.startsWith("^form")){

				}
				lineNo++;
			}

			// Flush at the end
			if (prevFieldName != null) {
				endLine = lineNo;
				String[] fieldLines = Arrays.copyOfRange(lines, startLine, endLine);

				TagInfo tagInfo = new TagInfo();
				tagInfo.setTagKey(prevFieldName.replaceAll("\\r",""));
				tagInfo.setTagValue(Arrays.stream(fieldLines).map(v -> v.replaceAll("\\t","").replaceAll("\\r","").trim()).collect(Collectors.joining(",")));
				tagInfoList.add(tagInfo);
			}

			tagInfoList.forEach((k) -> System.out.println("Key: "+k.getTagKey()+ " Value::::"+k.getTagValue()));

		} catch (Exception e) {
			Log.debug("CorrespondenceXMLCreationHandler: Exception in CorrespondenceXMLCreationHandler.process");
			Log.error(e);
		}

		return tagInfoList;
	}



















