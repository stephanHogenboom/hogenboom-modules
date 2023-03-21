package learning.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;

import static io.vavr.API.For;


public class ForComprehensionTest {

	public static void main(String[] args) {
		System.out.println(testForComprehension(1, EitherTestingInsert.RIGHT, "valid string"));
		System.out.println(testForComprehension(-1, EitherTestingInsert.RIGHT, "valid string"));

	}

	public static Tuple testForComprehension(int number, EitherTestingInsert insert, String message) {
		var tuples =  For(
				testForComprehension(number),
				testForComprehension(insert),
				testForComprehension(message)
		).yield(Tuple::of);

		if (tuples.hasNext()) {
			return tuples.next();
		}
		if (tuples.isLazy()) {
			System.out.println("here");
			tuples.forEach(t -> System.out.println(t._1 + t._2 + t._3));
		}
		return new Tuple3<>("", "", "");
	}


	private boolean processArguments(String s1, String s2, String s3) {
		return s1 != null && !s1.isBlank() && s2 != null && !s2.isBlank() && s3 != null && !s3.isBlank();
	}

	private static Either<EitherTestException, String> testForComprehension(EitherTestingInsert insert) {
		return switch (insert) {
			case LEFT ->  Either.left(new EitherTestException());
			case BOTH, RIGHT -> Either.right("SUCCES!");
		};
	}


	private static Either<EitherTestException, String> testForComprehension(int insert) {
		if (insert < 0) return Either.left(new EitherTestException("%s is negative!".formatted(insert)));
		return Either.right("SUCCESS!");
	}

	private static Either<EitherTestException, String> testForComprehension(String insert) {
		if (insert == null || insert.isBlank()) {
			return Either.left(new EitherTestException("%s cannot be blank".formatted(insert)));
		}
		return Either.right("SUCCES!");

	}




}
