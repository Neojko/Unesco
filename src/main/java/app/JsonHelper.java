package app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Paths;

public final class JsonHelper {
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  private JsonHelper() {}

  public static <T> T build(final String loc, final Class<T> target) throws IOException {
    return mapper.readValue(Paths.get(loc).toFile(), target);
  }

  public static String toString(final Object element) throws JsonProcessingException {
    return mapper.writeValueAsString(element);
  }
}
