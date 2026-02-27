package extentions;
import extentions.ConfigExtension;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ConfigExtension.class})
public abstract class BaseTest {

    @AfterEach
    //@Description("Удаление сущностей")
    public void afterEach() {
        //AbstractEntity.deleteCurrentTestEntities();
    }

    @AfterAll
    public static void afterAll() {
       // AbstractEntity.deleteCurrentClassEntities();
       // new DeleteOrderHelper().deleteOrders("DEVBOX", "damaged");
    }

//    @SneakyThrows
//    protected static <T> EntitySupplier<T> lazy(Supplier<T> executable) {
//        return new EntitySupplier<>(executable);
//    }
}