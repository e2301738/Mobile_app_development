package fi.christian.assignment_5_optional;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;

public class CatalogHandler {
    public static final String SEPARATOR = ";";
    public static final String SPACE = " ";
    public static final String NEW_LINE= "\n";
    public static final String SEARCH_CHOICE_FIRST_NAME = "FIRST_NAME";
    public static final String SEARCH_CHOICE_LAST_NAME = "LAST_NAME";
    public static final String SEARCH_CHOICE_PHONE = "PHONE";
    public static final String SEARCH_CHOICE_EDUCATION = "EDUCATION";
    public static ArrayList<String> firstNameList = new ArrayList<>();
    public static ArrayList<String> lastNameList = new ArrayList<>();
    public static ArrayList<String> educationList = new ArrayList<>();
    public static ArrayList<String> phoneList = new ArrayList<>();

    public static boolean isDuplicatePhone(String phone) {
        String searchPrefix = phone + SEPARATOR;
        for (String entry : phoneList) {
            if (entry.startsWith(searchPrefix)) {
                return true;
            }
        }
        return false;
    }

    public static void addEntry(Person person) {
        String hobbiesString = TextUtils.join(", ", person.getHobbies());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(person.getFirstName()).append(SEPARATOR)
                .append(person.getLastName()).append(SEPARATOR)
                .append(person.getPhone()).append(SEPARATOR)
                .append(person.getEducation()).append(SEPARATOR)
                .append(hobbiesString);
        firstNameList.add(stringBuilder.toString());

        stringBuilder.setLength(0);
        stringBuilder.append(person.getLastName()).append(SEPARATOR)
                .append(person.getFirstName()).append(SEPARATOR)
                .append(person.getPhone()).append(SEPARATOR)
                .append(person.getEducation()).append(SEPARATOR)
                .append(hobbiesString);
        lastNameList.add(stringBuilder.toString());

        stringBuilder.setLength(0);
        stringBuilder.append(person.getPhone()).append(SEPARATOR)
                .append(person.getFirstName()).append(SEPARATOR)
                .append(person.getLastName()).append(SEPARATOR)
                .append(person.getEducation()).append(SEPARATOR)
                .append(hobbiesString);
        phoneList.add(stringBuilder.toString());

        stringBuilder.setLength(0);
        stringBuilder.append(person.getEducation()).append(SEPARATOR)
                .append(person.getFirstName()).append(SEPARATOR)
                .append(person.getLastName()).append(SEPARATOR)
                .append(person.getPhone()).append(SEPARATOR)
                .append(hobbiesString);
        educationList.add(stringBuilder.toString());

    }

    public static String getFormattedResult(String catalogString, String searchChoice, Context context) {
        String[] catalogSplitArray = catalogString.split(SEPARATOR, -1);

        StringBuilder stringBuilder = new StringBuilder();
        String firstNameLabel = context.getString(R.string.first_name_label);
        String lastNameLabel = context.getString(R.string.last_name_label);
        String phoneNumberLabel = context.getString(R.string.phone_label);
        String educationLabel = context.getString(R.string.education_label);
        String hobbiesLabel = context.getString(R.string.hobbies_label);

        String hobbies = catalogSplitArray[4];
        if (hobbies.isEmpty()) {
            hobbies = context.getString(R.string.hobby_none);
        }

        switch (searchChoice) {
            case SEARCH_CHOICE_FIRST_NAME:
                stringBuilder.append(firstNameLabel).append(SPACE).append(catalogSplitArray[0]).append(NEW_LINE)
                        .append(lastNameLabel).append(SPACE).append(catalogSplitArray[1]).append(NEW_LINE)
                        .append(phoneNumberLabel).append(SPACE).append(catalogSplitArray[2]).append(NEW_LINE)
                        .append(educationLabel).append(SPACE).append(catalogSplitArray[3]).append(NEW_LINE)
                        .append(hobbiesLabel).append(SPACE).append(hobbies);
                break;
            case SEARCH_CHOICE_LAST_NAME:
                stringBuilder.append(lastNameLabel).append(SPACE).append(catalogSplitArray[0]).append(NEW_LINE)
                        .append(firstNameLabel).append(SPACE).append(catalogSplitArray[1]).append(NEW_LINE)
                        .append(phoneNumberLabel).append(SPACE).append(catalogSplitArray[2]).append(NEW_LINE)
                        .append(educationLabel).append(SPACE).append(catalogSplitArray[3]).append(NEW_LINE)
                        .append(hobbiesLabel).append(SPACE).append(hobbies);
                break;
            case SEARCH_CHOICE_PHONE:
                stringBuilder.append(phoneNumberLabel).append(SPACE).append(catalogSplitArray[0]).append(NEW_LINE)
                        .append(firstNameLabel).append(SPACE).append(catalogSplitArray[1]).append(NEW_LINE)
                        .append(lastNameLabel).append(SPACE).append(catalogSplitArray[2]).append(NEW_LINE)
                        .append(educationLabel).append(SPACE).append(catalogSplitArray[3]).append(NEW_LINE)
                        .append(hobbiesLabel).append(SPACE).append(hobbies);
                break;
            case SEARCH_CHOICE_EDUCATION:
                stringBuilder.append(educationLabel).append(SPACE).append(catalogSplitArray[0]).append(NEW_LINE)
                        .append(firstNameLabel).append(SPACE).append(catalogSplitArray[1]).append(NEW_LINE)
                        .append(lastNameLabel).append(SPACE).append(catalogSplitArray[2]).append(NEW_LINE)
                        .append(phoneNumberLabel).append(SPACE).append(catalogSplitArray[3]).append(NEW_LINE)
                        .append(hobbiesLabel).append(SPACE).append(hobbies);
                break;
        }
        return stringBuilder.toString();
    }
}
