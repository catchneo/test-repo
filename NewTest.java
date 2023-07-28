private static List<TagInfo> processData1(String data) {
        String[] lines = data.split("\n");
        String EXCLUDE = "^field IMAGE";

        System.out.println(lines.length);
        List<TagInfo> tagInfoList = new ArrayList<>();
        int lineNo = 0;

        String prevFieldName = null;
        TagInfo parentTagInfo = null;
        int startLine = 0, endLine = 0;

        while (lineNo < lines.length) {
            String val = lines[lineNo].trim().replaceAll("\t", "");
            if ((val.startsWith("^field") || val.startsWith("^global") || val.startsWith("^graph"))
                    && !val.startsWith(EXCLUDE)) {

                if (prevFieldName != null) {
                    endLine = lineNo;
                    String[] fieldLines = Arrays.copyOfRange(lines, startLine, endLine);

                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagKey(prevFieldName.replaceAll("\\r",""));
                    tagInfo.setTagValue(Arrays.stream(fieldLines).map(v -> v.replaceAll("\\t","").replaceAll("\\r","").trim()).collect(Collectors.joining(",")));

                    if (parentTagInfo != null) {
                        if (parentTagInfo.getChildTags() == null) {
                            parentTagInfo.setChildTags(new ArrayList<>());
                        }
                        parentTagInfo.getChildTags().add(tagInfo);
                    } else {
                        tagInfoList.add(tagInfo);
                    }
                }

                prevFieldName = val.split(" ")[1];
                startLine = lineNo + 1;
            }
            else if(val.startsWith("^form")){
                if (lines[lineNo+1].contains("^field IMAGE")) {
                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagKey("form");
                    tagInfo.setTagValue(val.split(" ")[1].replaceAll("\\r",""));
                    tagInfoList.add(tagInfo);

                    parentTagInfo = null;
                    prevFieldName = null;
                    startLine = lineNo + 1;

                } else {
                    parentTagInfo = new TagInfo();
                    parentTagInfo.setTagKey("form");
                    parentTagInfo.setTagValue(val.split(" ")[1].replaceAll("\\r",""));
                    tagInfoList.add(parentTagInfo);
                    startLine = lineNo + 1;
                }
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
            if (parentTagInfo != null) {
                if (parentTagInfo.getChildTags() == null) {
                    parentTagInfo.setChildTags(new ArrayList<>());
                }
                parentTagInfo.getChildTags().add(tagInfo);
            } else {
                tagInfoList.add(tagInfo);
            }
        }

        tagInfoList.forEach((k) -> System.out.println("Key: "+k.getTagKey()+ " Value::::"+k.getTagValue()));
        return tagInfoList;
    }
