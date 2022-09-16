package animals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Play {
    ObjectMapper objectMapper;
    String fileName;

    File file;
    private Node root;
    private Node endNode;

    private String fact;
    private String factVerb;
    private String factRest;

    private boolean keepPlaying = true;
    private final String[] consonants = {"a", "e", "i", "o","u", "y", "xe"};
    private final String[] exceptions = {"unicorn"}; // :3
    private final Scanner scn;
    private final String[] answerYes = {"y", "yes", "yeah", "yep", "sure", "right",
            "affirmative", "correct", "indeed", "you bet", "exactly", "you said it"};
    private final String[] answerNo = {"n", "no", "no way", "nah", "nope", "negative", "i don't think so", "yeah no"};

    private final String specifyFactPrompt = "Specify a fact that distinguishes %s from %s.\n" +
            "The sentence should be of the format: 'It can/has/is ...'.\n";

    private final String invalidFactPrompt = "The examples of a statement:\n" +
            " - It can fly\n" +
            " - It has horn\n" +
            " - It is a mammal";

    public Play() {

        this.scn = new Scanner(System.in);
    }

    public void launchFile() {
        fileName = String.format("animals.%s", Main.fileType);
        switch (Main.fileType) {
            case "json" : objectMapper = new JsonMapper();
            break;
            case "xml" : objectMapper = new XmlMapper();
            break;
            case "yaml" : objectMapper = new YAMLMapper();
            break;
            default: break;
        }
        file = new File(String.format(".\\%s",fileName));
    }

    public void greetings() {
        int currentHour = LocalTime.now().getHour();
        if(currentHour >= 5 && currentHour < 12) {
            System.out.println("Good morning!\n");
        } else if (currentHour >= 12 && currentHour < 18){
            System.out.println("Good afternoon!\n");
        } else {
            System.out.println("Good evening!\n");
        }
    }

    public void rootSetup(){
        System.out.println("I want to learn about animals.\n" +
                "Which animal do you like most?");
        root = new Node(processInput(scn.nextLine().toLowerCase()));

    }

    private boolean guess(Node node){
        if(node.isLeaf()){
            System.out.println("Is it " + node.getValue() + "?");
            endNode = node;
            return getYesNoAnswer();
        } else {
            System.out.println(node.getValue());
            if(getYesNoAnswer()){
                return guess(node.getYesChild());
            } else {
                return guess(node.getNoChild());
            }
        }
    }

    private void guessedCorrectly() {
        System.out.println("Yay!!! I did it! I'm so smart! :)\n");
    }

    private void guessedIncorrectly(){
        System.out.println("I give up. What animal do you have in mind?\n");
        String newAnimal = processInput(scn.nextLine().toLowerCase());
        specifyFact(endNode.getValue(), newAnimal);
        boolean isFactTrueForNewAnimal = askAboutDistinction(endNode.getValue(), newAnimal);
        String distinctionQuestion = getDistinctionQuestion();
        if(isFactTrueForNewAnimal){
            endNode.insertYesChild(newAnimal);
            endNode.insertNoChild(endNode.getValue());
        } else {
            endNode.insertNoChild(newAnimal);
            endNode.insertYesChild(endNode.getValue());
        }
        endNode.setValue(distinctionQuestion);
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.println(distinctionQuestion);
        System.out.println("Nice! I've learned so much about animals!\n");
    }

    private void askToContinue(){
        System.out.println("Would you like to play again?");
        if(!getYesNoAnswer()) {
            keepPlaying = false;
        }
    }

    public void gameStart() throws IOException {
        greetings();
        rootSetup();
        while(keepPlaying){
            System.out.println("Wonderful! I've learned so much about animals!\n" +
                    "Let's play a game!\n" +
                    "You think of an animal, and I guess it.\n" +
                    "Press enter when you're ready.");
            scn.nextLine();

            if(guess(root)){
                guessedCorrectly();
            } else {
                guessedIncorrectly();
            }
            askToContinue();
        }
        goodBye();

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(file, root);
    }

    public void specifyFact(String firstAnimal, String secondAnimal) {
        boolean validFact = false;
        while(!validFact){
            System.out.printf(specifyFactPrompt, firstAnimal, secondAnimal);
            fact = scn.nextLine().replaceAll("\\.","").toLowerCase();
            validFact = fact.matches("it (can|has|is)\\b.*");
            if(!validFact){
                System.out.println(invalidFactPrompt);
            }
        }
        Matcher verbMatcher = Pattern.compile("(can|has|is)").matcher(fact);
        if(verbMatcher.find()) {
            factVerb = verbMatcher.group();
            factRest = fact.replaceFirst("it (can|has|is) ","");
        }

    }

    public boolean askAboutDistinction(String firstAnimal, String secondAnimal){
        System.out.printf("Is the statement correct for %s?\n",secondAnimal);
        boolean isFactTrueForTheSecondAnimal = getYesNoAnswer();
        System.out.println("I learned the following facts about animals:");
        System.out.println(getAnimalFact(firstAnimal, !isFactTrueForTheSecondAnimal));
        System.out.println(getAnimalFact(secondAnimal, isFactTrueForTheSecondAnimal));
        return isFactTrueForTheSecondAnimal;
    }

    private String getAnimalFact(String animal, boolean isFactTrueForTheAnimal){
        StringBuilder animalFact = new StringBuilder();
        animalFact.append(" - The ").append(animal.replaceFirst("(a |an )",""));
        switch(factVerb){
            case "can":
                animalFact.append(isFactTrueForTheAnimal ? " can " : " can't ");
                break;
            case "has":
                animalFact.append(isFactTrueForTheAnimal ? " has " : " doesn't have ");
                break;
            case "is":
                animalFact.append(isFactTrueForTheAnimal ? " is " : " isn't ");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + factVerb);
        }
        animalFact.append(factRest).append(".");
        return animalFact.toString();
    }

    private String getDistinctionQuestion(){
        StringBuilder distinctionQuestion = new StringBuilder();
        distinctionQuestion.append(" - ");
        switch(factVerb){
            case "can":
                distinctionQuestion.append("Can it ");
                break;
            case "has":
                distinctionQuestion.append("Does it have ");
                break;
            case "is":
                distinctionQuestion.append("Is it ");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + factVerb);
        }
        distinctionQuestion.append(factRest).append("?");
        return distinctionQuestion.toString();
    }



    private String processInput(String input) {
        String output = input;
        for(String exception : exceptions){
            if(input.contains(exception) && (input.startsWith("a ")||
                    input.startsWith("an ")||
                    input.startsWith("the "))){
                return input;
            }
        }
        if(input.startsWith("a ")) {
            output = input.replaceFirst("a ", "");
        }
        if(input.startsWith("an ")) {
            output = input.replaceFirst("an ", "");
        }
        if(input.startsWith("the ")) {
            output = input.replaceFirst("the ", "");
        }
        boolean startsWithConsonant = false;
        for(String consonant : consonants) {
            if(output.startsWith(consonant)) {
                startsWithConsonant = true;
                break;
            }
        }
        return startsWithConsonant ? "an " + output : "a " + output;
    }


    private boolean getYesNoAnswer() {
        while(true) {
            String answerProcessed = scn.nextLine()
                    .replaceFirst("[.!]", "")
                    .trim()
                    .toLowerCase();
            for (String ans : answerYes) {
                if (ans.equals(answerProcessed)) {
                    return true;
                }
            }
            for (String ans : answerNo) {
                if (ans.equals(answerProcessed)) {
                    return false;
                }
            }
            clarify();
        }
    }

    private void clarify() {
        Random rand = new Random();
        switch(rand.nextInt(3)) {
            case 0:
                System.out.println("Come on, yes or no?");
                break;
            case 1:
                System.out.println("Is that, yes or no?");
                break;
            case 2:
                System.out.println("I don't understand, yes or no?");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rand.nextInt(2));
        }
    }

    public void goodBye() {
        Random rand = new Random();
        switch(rand.nextInt(2)) {
            case 0:
                System.out.println("Good bye!");
                break;
            case 1:
                System.out.println("See you later!");
                break;
            case 2:
                System.out.println("Until next time!");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rand.nextInt(2));
        }
    }
}
