package automation;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import library.CreatePost;

public class TC_001_E2EAutomation {
	
	@Test
	public void tcase1()
	{
		
		//Step 1 : Create a new Resource using json Object
		int input = 28;
		CreatePost cpost = new CreatePost();
		cpost.setId(input);
		cpost.setTitle("Title1");
		cpost.setAuthor("Author1");
		
		 ValidatableResponse response = given()
		 .contentType(ContentType.JSON)
		 .body(cpost)
		 
		 .when()
		 .post("http://localhost:3000/posts")
		
		//System.out.println(response.asString());
		.then()
		.contentType(ContentType.JSON);
		
		int responseId = response.extract().path("id");
		int actualStatusCode = response.extract().response().getStatusCode();
		
		Assert.assertEquals(actualStatusCode, 201);
		
		//Step 2 : Validate Resource created in step 1 using id / Validate its Title and Author
		
		ValidatableResponse response1 = when()
		.get("http://localhost:3000/posts/" + responseId)
		.then()
		.contentType(ContentType.JSON);
		
		String actTitle = response1.extract().path("title");
		String actauthor = response1.extract().path("author");
		
		Assert.assertEquals(actTitle, "Title1");
		Assert.assertEquals(actauthor, "Author1");
		
		
		//Step 3 : Update resource created in step 1 using PUT / Validate updated Title and Author
		
		CreatePost cpost1 = new CreatePost();
		cpost1.setId(input);
		cpost1.setAuthor("Updated Author");
		cpost1.setTitle("Updated Title");
		
		ValidatableResponse response2 = given()
		.contentType(ContentType.JSON)
		.body(cpost1)
		.when()
		.put("http://localhost:3000/posts/" + input)
		.then()
		.contentType(ContentType.JSON);
		
		//Step 3.1  Validate updated Title and Author
		
		ValidatableResponse response3 = when()
				.get("http://localhost:3000/posts/" + responseId)
				.then()
				.contentType(ContentType.JSON);
				
				String actTitle1 = response3.extract().path("title");
				String actauthor1 = response3.extract().path("author");
				
				Assert.assertEquals(actTitle1, "Updated Title");
				Assert.assertEquals(actauthor1, "Updated Author");
		
				
		//Step 4 : Delete resource created in step 1 using Delete / Validate resource is deleted
				
		when()
		.delete("http://localhost:3000/posts/" + input);
		
		//Step 4.1  Validate resource is deleted
		
		ValidatableResponse response4 = when()
				.get("http://localhost:3000/posts/" + responseId)
				.then()
				.contentType(ContentType.JSON);
		
		Assert.assertNotEquals(response4.extract().response().statusCode(), 201);
	}

}
