package org.findy.findy_be.common.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.bookmark.dto.request.CategoryRequest;
import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;

class BulkInsertRepositoryImplTest extends RepositoryTest {

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private BulkInsertRepositoryImpl<Place> placeCustomRepository;

	@Autowired
	private EntityManager entityManager;

	private List<Place> testPlaces;

	@AfterEach
	void tearDown() {
		placeRepository.deleteAll();
		entityManager.clear();
	}

	private List<Place> generateTestPlaces(int entityCount) {
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		return IntStream.range(0, entityCount)
			.mapToObj(i -> new RegisterPlaceRequest(
				"Test Place " + i,
				"Description " + i,
				"02-1234-5678",
				"Seoul Road " + i,
				categoryRequest,
				"1269827323",
				"375719345",
				"02-000-000"

			))
			.map(Place::create)
			.collect(Collectors.toList());
	}

	@DisplayName("saveAll() 성능 테스트")
	@ParameterizedTest
	@ValueSource(ints = {10, 100, 1000, 10000})
	void testSaveAllPerformance(int entityCount) {
		testPlaces = generateTestPlaces(entityCount);

		long startTime = System.nanoTime();
		placeRepository.saveAll(testPlaces);
		long endTime = System.nanoTime();

		System.out.println(
			"Time taken by saveAll() with " + entityCount + " entities: " + (endTime - startTime) / 1_000_000 + " ms");
	}

	@DisplayName("bulkInsertPlaces() 성능 테스트")
	@ParameterizedTest
	@ValueSource(ints = {10, 100, 1000, 10000})
	void testBulkInsertPlacesPerformance(int entityCount) {
		testPlaces = generateTestPlaces(entityCount);

		long startTime = System.nanoTime();
		placeCustomRepository.bulkInsert(testPlaces);
		long endTime = System.nanoTime();

		System.out.println(
			"Time taken by bulkInsertPlaces() with " + entityCount + " entities: " + (endTime - startTime) / 1_000_000
				+ " ms");
	}
}