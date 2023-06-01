package com.sytoss.edu.elevator;

import com.sytoss.edu.elevator.commands.*;
import com.sytoss.edu.elevator.converters.HouseConverter;
import com.sytoss.edu.elevator.converters.ShaftConverter;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.repositories.HouseRepository;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.FloorService;
import com.sytoss.edu.elevator.services.HouseService;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Comparator;
import java.util.List;

import static com.sytoss.edu.elevator.HouseThreadPool.*;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;


@Suite
@IncludeEngines("cucumber")
@CucumberContextConfiguration
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberOptions(features = "src/test/resources/features", glue = "com.sytoss.edu.elevator", tags = "not @SingleRun")
@Getter
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class IntegrationTest extends AbstractControllerTest {

    @Autowired
    private FloorService floorService;

    @Autowired
    private FindNearestCabinCommand findNearestCabinCommand;

    @Autowired
    private PressUpButtonCommand pressUpButtonCommand;

    @Autowired
    @SpyBean
    private CommandManager commandManager;

    @Autowired
    @SpyBean
    private CloseDoorCommand closeDoorCommand;

    @Autowired
    @SpyBean
    private OpenDoorCommand openDoorCommand;

    @Autowired
    @SpyBean
    private StartEngineCommand startEngineCommand;

    @Autowired
    @SpyBean
    private StopEngineCommand stopEngineCommand;

    @Autowired
    @SpyBean
    private MoveCabinCommand moveCabinCommand;

    @Autowired
    @SpyBean
    private VisitFloorCommand visitFloorCommand;

    @Autowired
    private HouseThreadPool houseThreadPool;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ShaftRepository shaftRepository;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseConverter houseConverter;

    @Autowired
    private ShaftConverter shaftConverter;

    protected List<ShaftDTO> getShaftDTOListByHouseId(long houseId) {
        List<ShaftDTO> shaftDTOlist = getShaftRepository().findByHouseDTOId(houseId);
        shaftDTOlist.sort(Comparator.comparingLong(ShaftDTO::getId));
        return shaftDTOlist;
    }

    protected long getHouseId(int houseIndex) {
        return TestContext.getInstance().getHouseIds().get(houseIndex);
    }

    protected long getShaftId(int houseIndex, int shaftIndex) {
        return TestContext.getInstance().getShaftIds().get(houseIndex).get(shaftIndex);
    }

    protected void await(int num) {
        int time = num * OPEN_DOOR_TIME_SLEEP + CLOSE_DOOR_TIME_SLEEP + VISIT_FLOOR_TIME_SLEEP + MOVE_CABIN_TIME_SLEEP + 20 * num;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
