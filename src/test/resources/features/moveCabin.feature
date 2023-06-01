Feature: move cabin

  Scenario: cabin is on the same floor
    Given All shaft are free and no sequence of stops in queue
    And shaft with index 0 has free cabin and cabin position 3
    And shaft with index 1 has free cabin and cabin position 7
    When passenger on floor 7 presses UpFloorButton with direction "UPWARDS"
    Then commands should have be invoked for shaft with index 1: OpenDoorCommand, CloseDoorCommand for floor 7

  Scenario: calling a cabin for both free cabins with direction "UPWARDS"
    Given All shaft are free and no sequence of stops in queue
    And shaft with index 0 has free cabin and cabin position 3
    And shaft with index 1 has free cabin and cabin position 5
    When passenger on floor 7 presses UpFloorButton with direction "UPWARDS"
    Then commands should have be invoked for shaft with index 1: StartEngineCommand, StopEngineCommand, OpenDoorCommand, CloseDoorCommand for floor 7

  Scenario: move cabin with sequence and with "UPWARDS" direction
    Given All shaft are free and no sequence of stops in queue
    And  shaft with index 1 has sequence of stops with floors 3, 5, 7 and Direction "UPWARDS" and cabin position 2
    And  shaft with index 0 has sequence of stops with floor 12 and Direction "UPWARDS" and cabin position 9
    When start cabin with index 1 moving sequence of stops to
    Then commands should have be invoked for shaft with index 1: StartEngineCommand, StopEngineCommand, OpenDoorCommand, CloseDoorCommand for floors 3, 5, 7

  Scenario: calling a cabins from different houses with all free shafts in first house and one busy shaft in second house and direction "UPWARDS"
    Given house 0 with numberOfFloors 16 and numberOfShafts 2
    And   house 1 with numberOfFloors 16 and numberOfShafts 2
    And   shaft 0 in house 0 has free cabin and cabin position 3
    And   shaft 1 in house 0 has free cabin and cabin position 4
    And   shaft 0 in house 1 has sequence of stops with floors 8, 9 and Direction "UPWARDS" and cabin position 4
    And   shaft 1 in house 1 has free cabin and cabin position 1
    And   shafts in house 1 are moving
    When passenger in house 0 presses UpFloorButton on floor 5
    Then commands should have be invoked for shaft 0 in house 1: MoveCabinCommand, StopEngineCommand, OpenDoorCommand, CloseDoorCommand for floors 8, 9
    And  command should have be invoked for shaft 0 in house 1: VisitFloorCommand for floors 5, 6, 7, 8, 9
    And  shaft 1 in house 0 has cabin position 5
    And  commands should have be invoked for shaft 1 in house 0: MoveCabinCommand, VisitFloorCommand, StopEngineCommand, OpenDoorCommand, CloseDoorCommand for floor 5
    And  shaft 0 in house 1 has cabin position 9