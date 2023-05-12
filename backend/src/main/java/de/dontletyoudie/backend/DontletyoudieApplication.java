package de.dontletyoudie.backend;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.dontletyoudie.backend.dummydata.AccountCsvObject;
import de.dontletyoudie.backend.dummydata.RelationshipJasonObject;
import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountRepository;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.JudgementService;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import de.dontletyoudie.backend.persistence.relationship.Relationship;
import de.dontletyoudie.backend.persistence.relationship.RelationshipRepository;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

@SpringBootApplication
public class DontletyoudieApplication {

    /**
     * Set this to false to avoid the insertion of dummy data at startup
     * (imported if the database will not be newly created at startup)
     */
    public static boolean GENERATE_NEW_DATABASE_ENTRIES = true;


    // this shouldbe removed in the final version!!!!!
    private static final ObjectMapper mapper = new ObjectMapper();
    private static RelationshipRepository relationshipRepo;
    private static AccountService accountService;
    private static RelationshipService relationshipService;
    private static final Hashtable<Long, Account> accounts = new Hashtable<>();
    private static ProofService proofService;
    private static JudgementService judgementService;
    private static final Random ran = new Random();
    private static byte[] img;

    public static void main(String[] args) {
        SpringApplication.run(DontletyoudieApplication.class, args);


        //generateRealations();
    }

    private static void generateRealations() {
        Set<RelationshipJasonObject> list = new HashSet<>();
        while (list.size() < 100) {
            RelationshipJasonObject e = new RelationshipJasonObject(ran.nextLong(57), ran.nextLong(57), (byte) ran.nextInt(2));
            if (e.a1 == 0 || e.a2 == 0 || e.a2 == e.a1) continue;
            list.add(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        list.stream().map(o -> {
                    try {
						return mapper.writeValueAsString(o);
                    } catch (Exception e) {
						return "";
					}
                }
        ).forEach(System.out::println);
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

        // Passi and michael0604 are friends by default
        relationshipService.save(new RelationshipAddDto("passi0305", "michael0604"));
        relationshipService.accept("passi0305", "michael0604");

        // Gloria has sent Michael a friend request which is pending
        relationshipService.save(new RelationshipAddDto("gloria0305", "michael0305"));

        // Michael has sent Passi a friend request which is pending
        relationshipService.save(new RelationshipAddDto("michael0305", "passi0305"));
    }

    private static void insertBulkDataFromFile()
            throws Exception {

        loadImg();

        insertAccounts();

        insertRels();

        insertProofs();

        insertJudgements();
        //generateJudgements();

    }

    private static void insertAccounts() throws IOException, AccountAlreadyExistsException, AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        CsvMapper csvMapper = new CsvMapper();
        ObjectReader oReader = csvMapper.reader(AccountCsvObject.class).with(CsvSchema.emptySchema().withHeader());

        BufferedReader reader = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/accounts.csv"));
        MappingIterator<AccountCsvObject> mi = oReader.readValues(reader);
        while (mi.hasNext()) {
            AccountCsvObject current = mi.next();
            Account account = accountService.createAccount(new AccountAddDTO(current.benutzername, current.email, current.passwort));
            accounts.put(account.getId(), account);
        }

        relationshipService.save(new RelationshipAddDto("richbitch", "booster"));
        relationshipService.accept("richbitch", "booster");
    }

    private static void insertRels() throws IOException {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/rels.txt"))) {

            reader2.lines()
                    .map(DontletyoudieApplication::mapStringToRelationJasonObject)
                    .map(j -> new Relationship(accounts.get(j.a1), accounts.get(j.a2), j.status))
                    .forEach(relationshipRepo::save);
        } catch (Exception e) {
            throw e;
        }
    }

    private static RelationshipJasonObject mapStringToRelationJasonObject(String l) {
        try {
            return mapper.readValue(l, RelationshipJasonObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadImg() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("backend/src/main/resources/dummydata/meme.jpg"));
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        img = data.getData();
    }

    private static void insertProofs() throws IOException {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/proof.txt"))) {
            reader2.lines()
                    .map(l -> new ProofAddDto(l, img, ZonedDateTime.now(), "", ""))
                    .forEach(DontletyoudieApplication::saveProof);
        }
    }

    private static void saveProof(ProofAddDto p) {
        try {
            proofService.saveProof(p);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertJudgements() throws IOException {
        try (BufferedReader reader2 = new BufferedReader(new FileReader("backend/src/main/resources/dummydata/judgements.txt"))) {
            reader2.lines()
                    .map(DontletyoudieApplication::mappStringToJudgementDto)
                    .forEach(DontletyoudieApplication::saveJudgement);
        }
    }

    private static void saveJudgement(JudgementDto j) {
        try {
            judgementService.saveJudgement(j);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static JudgementDto mappStringToJudgementDto(String l) {
        try {
            return mapper.readValue(l, JudgementDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateJudgements() throws AccountNotFoundException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (Account account : accounts.values()) {
            int n = ran.nextInt(0, 5);
            int i;
            for (i = 0; i < n; i++) {
                Optional<ProofReturnDto> pendingProofs = proofService.getPendingProofs(account.getUsername());
                if (pendingProofs.isEmpty()) {
                    break;
                }

                JudgementDto judgementDto = new JudgementDto(account.getUsername(), pendingProofs.get().getProofId(), ran.nextInt(0, 100) < 75);
                //list.add(judgementDto);
                System.out.println(mapper.writeValueAsString(judgementDto));
            }
        }
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService, RelationshipService relationshipService, RelationshipRepository relationshipRepository,
                                        ProofService proofService, JudgementService judgementService) {
        DontletyoudieApplication.accountService = accountService;
        DontletyoudieApplication.relationshipService = relationshipService;
        DontletyoudieApplication.relationshipRepo = relationshipRepository;
        DontletyoudieApplication.proofService = proofService;
        DontletyoudieApplication.judgementService = judgementService;
        return args -> {
            try {
                relationshipService.configureRelationTable();
                if (!GENERATE_NEW_DATABASE_ENTRIES) return;
                oldDummyData();
                insertBulkDataFromFile();
            } catch (Exception e) {
                System.out.println("Etwas ist schiefgelaufen, beim einspielen der Testdaten");
                e.printStackTrace();
            }
        };
    }
}
