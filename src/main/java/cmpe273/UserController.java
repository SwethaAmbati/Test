package cmpe273;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
public class UserController {

	// create user using POST
	private static final Map<Integer, User> userInfo = new HashMap<Integer, User>();
	private static final Map<Integer, Map<Integer, IDCard>> cardInfo = new HashMap<Integer, Map<Integer, IDCard>>();
	private static final Map<Integer, Map<Integer, WebLogin>> loginInfo = new HashMap<Integer, Map<Integer, WebLogin>>();
	private static final Map<Integer, Map<Integer, BankAccount>> bankInfo = new HashMap<Integer, Map<Integer, BankAccount>>();

	Random random = new Random();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSS");
	DateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
	Date date = new Date();
	Calendar calobj = Calendar.getInstance();
	
	Gson gson = new Gson();

	@RequestMapping(value = "/users/create", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@Valid @RequestBody User user, BindingResult result) {
		
		if (result.hasErrors()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
		
		user.setUserId( random.nextInt(1000));
	//	user.setCreated_at(dateFormat.format(date));

		user.setCreated_at(dateFormat.format(calobj.getTime()));
		userInfo.put(user.getUserId(), user);
		
		JsonObject jObject = new JsonObject();
		jObject.addProperty("userId", user.getUserId());

		return new ResponseEntity<String>(gson.toJson(jObject), HttpStatus.CREATED);
		
	}
	// view user details using GET

	@RequestMapping(value = "/users/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<User> viewUser(@PathVariable int user_id) {
		
		
		User user = userInfo.get(user_id);
		user.setUpdated_at(dateFormat.format(date));
		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	// update a specific user using PUT

	@RequestMapping(value = "/users/{user_id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable int user_id,
			@RequestBody User user) {

		userInfo.get(user_id).setEmail(user.getEmail());
		userInfo.get(user_id).setPassword(user.getPassword());

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// IDCARD

	// create id card

	@RequestMapping(value = "/users/{user_id}/idcards", method = RequestMethod.POST)
	public ResponseEntity<IDCard> createIdCard(@RequestBody IDCard idcard,
			@PathVariable int user_id) {

	
		idcard.setCard_id(random.nextInt(1000));

		if (cardInfo.get(user_id) == null) {
			Map<Integer, IDCard> idcardmap = new HashMap<Integer, IDCard>();
			idcardmap.put(idcard.getCard_id(), idcard);
			cardInfo.put(user_id, idcardmap);
		} else {
			cardInfo.get(user_id).put(idcard.getCard_id(), idcard);
		}

		return new ResponseEntity<IDCard>(idcard, HttpStatus.CREATED);
	}

	// list all id cards

	@RequestMapping(value = "/users/{user_id}/idcards", method = RequestMethod.GET)
	public ResponseEntity<String> listIdCard(@PathVariable int user_id) {
		
		//For testing only
		/*user_id = 1212;
		
		User tempUser = new User();
		tempUser.setUserId(123);
		tempUser.setName("testuser");
		tempUser.setEmail("test@test.com");
		
		
		userInfo.put(tempUser.getUserId(), tempUser);
		
		IDCard tempCard = new IDCard();
		tempCard.setCard_id(111);
		tempCard.setCard_name("test id name");
		tempCard.setCard_number("id card 1");
		tempCard.setExpiration_date("12/2/2014");
		
		IDCard tempCard1 = new IDCard();
		tempCard1.setCard_id(222);
		tempCard1.setCard_name("test id name 1");
		tempCard1.setCard_number("id card 2");
		tempCard1.setExpiration_date("11/2/2014");
		
		Map<Integer, IDCard> tempCardMap = new HashMap<Integer, IDCard>();
		tempCardMap.put(tempCard.getCard_id(), tempCard);
		tempCardMap.put(tempCard1.getCard_id(), tempCard1);
		
		cardInfo.put(user_id, tempCardMap);*/
		//testing block end
		
		
		
		
		
		 Map<Integer, IDCard> idCard = cardInfo.get(user_id);
		 String jsonStr = gson.toJson(idCard);
         
		//return new ResponseEntity<IDCard>(idcard, HttpStatus.OK);
		 return new ResponseEntity<String>( jsonStr, HttpStatus.OK);
	}

	// Delete id card

	@RequestMapping(value = "/users/{user_id}/idcards/{card_id}", method = RequestMethod.DELETE)
	public ResponseEntity<IDCard> deleteIdCard(@PathVariable int card_id,
			@PathVariable int user_id) {

		cardInfo.get(user_id).remove(card_id);
		return new ResponseEntity<IDCard>(HttpStatus.NO_CONTENT);
	}

	// WEB LOGIN

	// Create Web Login

	@RequestMapping(value = "/users/{user_id}/weblogins", method = RequestMethod.POST)
	public ResponseEntity<WebLogin> createWebLogin(
			@RequestBody WebLogin weblogin, @PathVariable int user_id) {

		weblogin.setLogin_id(random.nextInt(1000));

		if (loginInfo.get(user_id) == null) {
			Map<Integer, WebLogin> webloginmap = new HashMap<Integer, WebLogin>();
			webloginmap.put(weblogin.getLogin_id(), weblogin);
			loginInfo.put(user_id, webloginmap);
		} else {
			loginInfo.get(user_id).put(weblogin.getLogin_id(), weblogin);
		}

		return new ResponseEntity<WebLogin>(weblogin, HttpStatus.OK);
	}

	/*
	 * // list all web logins
	 * 
	 * @RequestMapping(value = " /users/{user_id}/weblogins", method =
	 * RequestMethod.GET) public ResponseEntity<WebLogin>
	 * listWebLogin(@RequestBody WebLogin weblogin) {
	 * 
	 * 
	 * return new ResponseEntity<WebLogin>(weblogin, HttpStatus.OK); }
	 */
	
	//Delete Web Login

	@RequestMapping(value = "/users/{user_id}/weblogins/{login_id}", method = RequestMethod.DELETE)
	public ResponseEntity<WebLogin> deleteWebLogin(@PathVariable int login_id,
			@PathVariable int user_id) {

		loginInfo.get(user_id).remove(login_id);

		return new ResponseEntity<WebLogin>(HttpStatus.NO_CONTENT);
	}

	// BANK ACCOUNT

	// Create Bank Account

	@RequestMapping(value = "/users/{user_id}/bankaccounts", method = RequestMethod.POST)
	public ResponseEntity<BankAccount> createBankAccount(
			@RequestBody BankAccount bankaccount, @PathVariable int user_id) {

		bankaccount.setBa_id(random.nextInt(1000));

		if (bankInfo.get(user_id) == null) {
			Map<Integer, BankAccount> bankmap = new HashMap<Integer, BankAccount>();
			bankmap.put(bankaccount.getBa_id(), bankaccount);
			bankInfo.put(user_id, bankmap);
		} else {
			bankInfo.get(user_id).put(bankaccount.getBa_id(), bankaccount);
		}

		return new ResponseEntity<BankAccount>(bankaccount, HttpStatus.OK);
	}

	/*
	 * // list all bank accounts
	 * 
	 * @RequestMapping(value = " /users/{user_id}/bankaccounts", method =
	 * RequestMethod.GET) public ResponseEntity<BankAccount>
	 * listBankAccount(@RequestBody BankAccount bankaccount) {
	 * 
	 * 
	 * return new ResponseEntity<BankAccount>(bankaccount, HttpStatus.OK); }
	 */
	// Delete Bank Account

	@RequestMapping(value = "/users/{user_id}/BankAccount/{ba_id}", method = RequestMethod.DELETE)
	public ResponseEntity<BankAccount> deleteBankAccount(
			@PathVariable int ba_id, @PathVariable int user_id) {

		bankInfo.get(user_id).remove(ba_id);

		return new ResponseEntity<BankAccount>(HttpStatus.OK);
	}

}
