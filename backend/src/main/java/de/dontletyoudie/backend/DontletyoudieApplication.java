package de.dontletyoudie.backend;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.dontletyoudie.backend.dummydata.AccountCsvObject;
import de.dontletyoudie.backend.dummydata.JudgementJasonObject;
import de.dontletyoudie.backend.dummydata.ProofJasonObeject;
import de.dontletyoudie.backend.dummydata.RelationshipJasonObject;
import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.category.CategoryService;
import de.dontletyoudie.backend.persistence.category.exceptions.CategoryNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.JudgementService;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.persistence.proof.Proof;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.exceptions.ProofNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.Relationship;
import de.dontletyoudie.backend.persistence.relationship.RelationshipRepository;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class DontletyoudieApplication {

    /**
     * Set this to false to avoid the insertion of dummy data at startup
     * (imported if the database will not be newly created at startup)
     */
    public static boolean BUILD_DATABASE_WITH_BULK_DUMMY_DATA = true;

    /**
     * 0 = nix, 1 = nur judgements, 2 = proofs und judgements, 3 = rels, proofs und judgements
     */
    public static byte GENERATE_NEW_DATA = 0;


    // this shouldbe removed in the final version!!!!!
    private static final ObjectMapper mapper = new ObjectMapper();
    private static RelationshipRepository relationshipRepo;
    private static AccountService accountService;
    private static RelationshipService relationshipService;
    private static final Hashtable<Long, Account> accounts = new Hashtable<>();
    private static final String[] categories = {"hunger", "thirst", "sleep","fitness", "cooking"};
    private static ProofService proofService;
    private static JudgementService judgementService;
    private static CategoryService categoryService;
    private static final Random ran = new Random();
    private static byte[] img;
    private static final List<Short> times = new ArrayList<>();
    private static final Set<RelationshipJasonObject> relSet = new HashSet<>();

    public static void main(String[] args) {
        mapper.findAndRegisterModules();
        SpringApplication.run(DontletyoudieApplication.class, args);
    }

    private static void generateRealations() throws Exception {
        long b = 0;
        for (Account a : accounts.values()) {
            if (a.getUsername().equals("booster")) {
                b = a.getId();
                break;
            }
        }

        try (BufferedWriter reader2 = new BufferedWriter(new FileWriter("backend/src/main/resources/dummydata/rels2.txt"))) {

            while (relSet.size() < 300) {
                RelationshipJasonObject e = new RelationshipJasonObject(
                        ran.nextLong(accounts.size()),
                        ran.nextLong(accounts.size()),
                        (byte) ran.nextInt(2));
                if (e.a1 == 0 || e.a2 == 0 || e.a2 == e.a1 || e.a1 == b || e.a2 == b) continue;
                if (!relSet.add(e)) continue;
                reader2.write(mapper.writeValueAsString(e));
                reader2.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
        System.out.println("fertig mit rels generieren");
    }

    private static void generateProofs() throws IOException {
        Set<ProofJasonObeject> list = new HashSet<>();
        for (int i = 3; i >= 0; i--) {
            LocalDateTime dateTime = LocalDateTime.of(2023, 5, LocalDate.now().getDayOfMonth()-i, 0, 0, 0);
            Collections.shuffle(times);
            generateOneDayOfProofs(times, list, dateTime);
        }

        try (BufferedWriter reader2 = new BufferedWriter(new FileWriter("backend/src/main/resources/dummydata/proofs2.txt"))) {

            list.stream()
                    .sorted()
                    .map(p -> {
                        try {
                            return mapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(s -> {
                        try {
                            reader2.write(s);
                            reader2.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw e;
        }
        System.out.println("fertig mit Proofs generieren");
    }

    private static void readTimes() throws IOException {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/randomTime.txt"))) {

            reader2.lines()
                    .map(Short::parseShort)
                    .forEach(times::add);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void generateOneDayOfProofs(List<Short> times, Set<ProofJasonObeject> list, LocalDateTime dateTime) {
        List<Account> values = new ArrayList<>(accounts.values());
        for (short t : times) {
            dateTime = dateTime.plusSeconds(t);

            int i;
            do {

                i = ran.nextInt(values.size());
            } while (values.get(i).getUsername().equals("booster"));

            int j = ran.nextInt(categories.length);
            ProofJasonObeject e = new ProofJasonObeject(values.get(i).getUsername(), categories[j], dateTime);
            list.add(e);
        }
    }

    private static void oldDummyData()
            throws Exception {
        Account account = accountService.createAccount(new AccountAddDTO("passi0305", "nichtpassis@e.mail",
                "passi007"
        ));
        accounts.put(account.getId(), account);
        account = accountService.createAccount(new AccountAddDTO("michael0305", "nichtmichael@e.mail",
                "michael007"
        ));
        accounts.put(account.getId(), account);
        account = accountService.createAccount(new AccountAddDTO("gloria0305", "nichtgloria@e.mail",
                "gloria007"
        ));
        accounts.put(account.getId(), account);
        account = accountService.createAccount(new AccountAddDTO("michael0604", "vielleictdochmichael@e.mail",
                "michael007"
        ));
        accounts.put(account.getId(), account);
        account = accountService.createAccount(new AccountAddDTO("gloria0306", "vielleichtdochgloria@e.mail",
                "gloria007"
        ));
        accounts.put(account.getId(), account);

        // Passi and Gloria are friends by default
        relationshipService.save(new RelationshipAddDto("passi0305", "gloria0305"));
        relationshipService.accept("passi0305", "gloria0305");
        relSet.add(new RelationshipJasonObject(
                accountService.getAccount("passi0305").getId(),
                accountService.getAccount("gloria0305").getId(),
                (byte) RelationshipStatus.FRIEND.ordinal()));


        // Passi and michael0604 are friends by default
        relationshipService.save(new RelationshipAddDto("passi0305", "michael0604"));
        relationshipService.accept("passi0305", "michael0604");
        relSet.add(new RelationshipJasonObject(
                accountService.getAccount("passi0305").getId(),
                accountService.getAccount("michael0604").getId(),
                (byte) RelationshipStatus.FRIEND.ordinal()));

        // Gloria has sent Michael a friend request which is pending
        relationshipService.save(new RelationshipAddDto("gloria0305", "michael0305"));
        relSet.add(new RelationshipJasonObject(
                accountService.getAccount("gloria0305").getId(),
                accountService.getAccount("michael0305").getId(),
                (byte) RelationshipStatus.PENDING.ordinal()));

        // Michael has sent Passi a friend request which is pending
        relationshipService.save(new RelationshipAddDto("michael0305", "passi0305"));
        relSet.add(new RelationshipJasonObject(
                accountService.getAccount("michael0305").getId(),
                accountService.getAccount("passi0305").getId(),
                (byte) RelationshipStatus.PENDING.ordinal()));
    }

    private static void insertBulkDataFromFile()
            throws Exception {

        loadImg();
        readTimes();

        insertAccounts();

        if (GENERATE_NEW_DATA > 2) generateRealations();
        insertRels();

        if (GENERATE_NEW_DATA > 1) generateProofs();
        insertProofs();

        if (GENERATE_NEW_DATA > 0) generateJudgements();
        insertJudgements();

        System.out.println("fertig mit generieren und einsielen");
    }

    private static String getDdlAuto() {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/application.properties"))) {

            Optional<String> first = reader2.lines()
                    .filter(s -> s.startsWith("spring.jpa.hibernate.ddl-auto="))
                    .map(s -> s.substring("spring.jpa.hibernate.ddl-auto=".length()))
                    .findFirst();
            if (first.isEmpty()) return "none";
            return first.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertAccounts() throws IOException, AccountAlreadyExistsException {
        CsvMapper csvMapper = new CsvMapper();
        ObjectReader oReader = csvMapper.reader(AccountCsvObject.class).with(CsvSchema.emptySchema().withHeader());

        BufferedReader reader = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/accounts.csv"));
        MappingIterator<AccountCsvObject> mi = oReader.readValues(reader);
        while (mi.hasNext()) {
            AccountCsvObject current = mi.next();
            Account account = accountService.createAccount(new AccountAddDTO(current.benutzername, current.email, current.passwort));
            accounts.put(account.getId(), account);
        }



        System.out.println("fertig mit Accounts einspielen");
    }

    private static void insertRels() throws Exception {
        relationshipService.save(new RelationshipAddDto("booster", "richbitch"));
        relationshipService.accept("richbitch", "booster");
        relationshipService.save(new RelationshipAddDto("booster", "golden_son"));
        relationshipService.accept("golden_son", "booster");
        relationshipService.save(new RelationshipAddDto("booster", "i_have_to_much_money"));
        relationshipService.accept("i_have_to_much_money", "booster");
        relationshipService.save(new RelationshipAddDto("booster", "jeff_bezos"));
        relationshipService.accept("jeff_bezos", "booster");
        relationshipService.save(new RelationshipAddDto("booster", "donald_trump"));
        relationshipService.accept("donald_trump", "booster");
        relationshipService.save(new RelationshipAddDto("booster", "donald_trump_jr"));
        relationshipService.accept("donald_trump_jr", "booster");

        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/rels2.txt"))) {

            reader2.lines()
                    .map(DontletyoudieApplication::mapStringToRelationJasonObject)
                    .map(j -> new Relationship(accounts.get(j.a1), accounts.get(j.a2), j.status))
                    .forEach(relationshipRepo::save);
        } catch (Exception e) {
            throw e;
        }

        System.out.println("fertig mit Rels einspielen");
    }

    private static RelationshipJasonObject mapStringToRelationJasonObject(String l) {
        try {
            return mapper.readValue(l, RelationshipJasonObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ProofJasonObeject mapStringToProofJasonObject(String l) {
        try {
            return mapper.readValue(l, ProofJasonObeject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadImg() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("backend/src/main/resources/dummydata/meme.jpg"));
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        img = data.getData();
        System.out.println("fertig mit Dummybild laden");
    }

    private static void insertProofs() throws IOException {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/proofs2.txt"))) {

            reader2.lines()
                    .map(DontletyoudieApplication::mapStringToProofJasonObject)
                    .map(j -> new ProofAddDto(j.getAccountname(), img, j.getDateTime(), j.getCategory(), ""))
                    .forEach(p -> {
                        try {
                            proofService.saveProof(p);
                        } catch (AccountNotFoundException | CategoryNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw e;
        }
        System.out.println("fertig mit Proofs einspielen");
    }

    private static void insertJudgements() throws Exception {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/judgements2.txt"))) {
            reader2.lines()
                    .map(DontletyoudieApplication::mappStringToJudgementDto)
                    .forEach(DontletyoudieApplication::saveJudgement);
        }


        Account boosteraccount = accountService.getAccount("booster");
        List<Proof> proofs = proofService.getAllPendingProofs("booster");
        for (Proof p : proofs) {

            saveJudgement(
                    new JudgementDto(
                            boosteraccount.getUsername(),
                            p.getId(),
                            true,
                            p.getDateTime().plusSeconds(times.get(ran.nextInt(times.size())))));
        }
        System.out.println("fertig mit Judgements einspielen");
    }

    private static void saveJudgement(JudgementDto j) {
        try {
            judgementService.saveJudgement(j);
        } catch (AccountNotFoundException | ProofNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static JudgementDto mappStringToJudgementDto(String l) {
        try {
            JudgementJasonObject jjo = mapper.readValue(l, JudgementJasonObject.class);
            return new JudgementDto(jjo.getJudge(), jjo.getProofId(), jjo.getApproved(), jjo.getDate());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateJudgements() throws AccountNotFoundException {
        List<JudgementJasonObject> list = new ArrayList<>();
        for (Account account : accounts.values()) {
            List<Proof> allPendingProofs = proofService.getAllPendingProofs(account.getUsername());
            if (allPendingProofs.size() == 0 || account.getUsername().equals("booster")) continue;

            Collections.shuffle(allPendingProofs);
            int n = ran.nextInt(allPendingProofs.size()/3, (allPendingProofs.size()*3)/4);
            int i;
            for (i = 0; i < n; i++) {
                Proof p = allPendingProofs.get(i);
                JudgementJasonObject jjo = new JudgementJasonObject(
                        account.getUsername(),
                        p.getId(),
                        ran.nextInt(0, 100) < 75,
                        p.getDateTime().plusSeconds(times.get(ran.nextInt(times.size()))));
                list.add(jjo);
            }
        }

        try (BufferedWriter reader2 = new BufferedWriter(new FileWriter("backend/src/main/resources/dummydata/judgements2.txt"))) {

            list.stream()
                    .sorted()
                    .map(p -> {
                        try {
                            return mapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(s -> {
                        try {
                            reader2.write(s);
                            reader2.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("fertig mit judgements generieren");
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService, RelationshipService relationshipService, RelationshipRepository relationshipRepository,
                                        ProofService proofService, JudgementService judgementService, CategoryService categoryService) {
        DontletyoudieApplication.accountService = accountService;
        DontletyoudieApplication.relationshipService = relationshipService;
        DontletyoudieApplication.relationshipRepo = relationshipRepository;
        DontletyoudieApplication.proofService = proofService;
        DontletyoudieApplication.judgementService = judgementService;
        DontletyoudieApplication.categoryService = categoryService;
        return args -> {
            System.out.println("beginne CommandlineRunner");
            String ddlAuto = getDdlAuto();
            if (!ddlAuto.equals("create") && !ddlAuto.equals("create-drop")) return;
            try {
                categoryService.createCategory("hunger", true);
                categoryService.createCategory("thirst", true);
                categoryService.createCategory("sleep", true);
                categoryService.createCategory("fitness", false);
                categoryService.createCategory("cooking", false);
                //relationshipService.configureRelationTable();
                System.out.println("beginne mit oldDummyData");
                oldDummyData();
                System.out.println("Fertig mit oldyDummy");
                if (!BUILD_DATABASE_WITH_BULK_DUMMY_DATA) return;
                System.out.println("beginne mit Massendummydaten");
                insertBulkDataFromFile();
                System.out.println("fertig mit Massendummydaten");
            } catch (Exception e) {
                System.out.println("Etwas ist schiefgelaufen, beim einspielen der Testdaten");
                e.printStackTrace();
            }
            System.out.println("fertig mit CommandlineRunner");
        };
    }
}
