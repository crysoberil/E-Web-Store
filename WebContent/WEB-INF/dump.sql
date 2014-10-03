SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `ewebstore` ;
CREATE SCHEMA IF NOT EXISTS `ewebstore` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `ewebstore` ;

-- -----------------------------------------------------
-- Table `ewebstore`.`District`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`District` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`District` (
  `districtID` BIGINT NOT NULL AUTO_INCREMENT,
  `districtName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`districtID`),
  UNIQUE INDEX `districtName_UNIQUE` (`districtName` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Branch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Branch` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Branch` (
  `branchID` BIGINT NOT NULL AUTO_INCREMENT,
  `branchName` VARCHAR(45) NOT NULL,
  `branchLocation` VARCHAR(80) NOT NULL,
  `branchDistrict` BIGINT NOT NULL,
  PRIMARY KEY (`branchID`),
  INDEX `fk_Branch_1_idx` (`branchDistrict` ASC),
  UNIQUE INDEX `branchName_UNIQUE` (`branchName` ASC),
  CONSTRAINT `fk_Branch_1`
    FOREIGN KEY (`branchDistrict`)
    REFERENCES `ewebstore`.`District` (`districtID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`BranchManager`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`BranchManager` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`BranchManager` (
  `branchID` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `gender` TINYINT(1) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `address` VARCHAR(100) NULL,
  `dob` DATE NOT NULL,
  `contactnumber` VARCHAR(15) NULL,
  `managerID` BIGINT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`managerID`),
  CONSTRAINT `fk_BranchManager_1`
    FOREIGN KEY (`branchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`SalesEmployee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`SalesEmployee` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`SalesEmployee` (
  `employeeID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `gender` TINYINT(1) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `contactNumber` VARCHAR(15) NOT NULL,
  `dob` DATE NOT NULL,
  `joinDate` DATE NOT NULL,
  `address` VARCHAR(100) NULL,
  `branchID` BIGINT NOT NULL,
  PRIMARY KEY (`employeeID`),
  INDEX `fk_SalesEmployee_1_idx` (`branchID` ASC),
  CONSTRAINT `fk_SalesEmployee_1`
    FOREIGN KEY (`branchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Customer` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Customer` (
  `customerID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `gender` TINYINT(1) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `address` VARCHAR(80) NULL,
  `contactNumber` VARCHAR(15) NOT NULL,
  `registrationDate` DATE NOT NULL,
  `premiumCustomer` TINYINT(1) NOT NULL,
  `totalTransaction` DOUBLE NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`customerID`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Brand` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Brand` (
  `brandID` BIGINT NOT NULL AUTO_INCREMENT,
  `brandName` VARCHAR(45) NOT NULL,
  `brandDetail` VARCHAR(150) NULL,
  PRIMARY KEY (`brandID`),
  UNIQUE INDEX `brandName_UNIQUE` (`brandName` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Product` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Product` (
  `productID` BIGINT NOT NULL AUTO_INCREMENT,
  `productName` VARCHAR(45) NOT NULL,
  `brandID` BIGINT NOT NULL,
  `productDetail` VARCHAR(500) NULL,
  `productImageLink` VARCHAR(100) NULL,
  `price` DOUBLE NOT NULL,
  PRIMARY KEY (`productID`),
  INDEX `fk_Product_2_idx` (`brandID` ASC),
  UNIQUE INDEX `productName_UNIQUE` (`productName` ASC),
  CONSTRAINT `fk_Product_2`
    FOREIGN KEY (`brandID`)
    REFERENCES `ewebstore`.`Brand` (`brandID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Category` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Category` (
  `categoryID` BIGINT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`categoryID`),
  UNIQUE INDEX `categoryName_UNIQUE` (`categoryName` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`BranchInventory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`BranchInventory` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`BranchInventory` (
  `branchID` BIGINT NOT NULL,
  `productID` BIGINT NOT NULL,
  `inStockQuantity` INT NOT NULL,
  `soldQuantity` INT NOT NULL,
  `withdrawnQuantity` INT NOT NULL,
  `availableQuantity` INT NOT NULL,
  INDEX `fk_BranchInventory_1_idx` (`branchID` ASC),
  INDEX `fk_BranchInventory_2_idx` (`productID` ASC),
  PRIMARY KEY (`branchID`, `productID`),
  CONSTRAINT `fk_BranchInventory_1`
    FOREIGN KEY (`branchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BranchInventory_2`
    FOREIGN KEY (`productID`)
    REFERENCES `ewebstore`.`Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`OrderStatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`OrderStatus` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`OrderStatus` (
  `orderStatusID` BIGINT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`orderStatusID`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`Order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`Order` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`Order` (
  `orderID` BIGINT NOT NULL AUTO_INCREMENT,
  `customerID` BIGINT NOT NULL,
  `orderDate` DATE NOT NULL,
  `detailedDeliveryLocation` VARCHAR(80) NOT NULL,
  `orderStatusID` BIGINT NOT NULL,
  `branchID` BIGINT NOT NULL,
  `associatedEmployee` BIGINT NULL,
  `totalOrderingCost` DOUBLE NOT NULL,
  PRIMARY KEY (`orderID`),
  INDEX `fk_Order_1_idx` (`customerID` ASC),
  INDEX `fk_Order_2_idx` (`associatedEmployee` ASC),
  INDEX `fk_Order_4_idx` (`orderStatusID` ASC),
  INDEX `fk_Order_3_idx` (`branchID` ASC),
  CONSTRAINT `fk_Order_1`
    FOREIGN KEY (`customerID`)
    REFERENCES `ewebstore`.`Customer` (`customerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_2`
    FOREIGN KEY (`associatedEmployee`)
    REFERENCES `ewebstore`.`SalesEmployee` (`employeeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_3`
    FOREIGN KEY (`branchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_4`
    FOREIGN KEY (`orderStatusID`)
    REFERENCES `ewebstore`.`OrderStatus` (`orderStatusID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`OrderProducts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`OrderProducts` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`OrderProducts` (
  `orderProductsID` BIGINT NOT NULL AUTO_INCREMENT,
  `orderID` BIGINT NOT NULL,
  `productID` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`orderProductsID`),
  INDEX `fk_OrderProducts_1_idx` (`orderID` ASC),
  INDEX `fk_OrderProducts_2_idx` (`productID` ASC),
  CONSTRAINT `fk_OrderProducts_1`
    FOREIGN KEY (`orderID`)
    REFERENCES `ewebstore`.`Order` (`orderID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrderProducts_2`
    FOREIGN KEY (`productID`)
    REFERENCES `ewebstore`.`Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`DistrictDistance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`DistrictDistance` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`DistrictDistance` (
  `district1ID` BIGINT NOT NULL,
  `district2ID` BIGINT NOT NULL,
  `distance` DOUBLE NOT NULL,
  INDEX `fk_DistrictDistance_1_idx` (`district1ID` ASC),
  INDEX `fk_DistrictDistance_2_idx` (`district2ID` ASC),
  PRIMARY KEY (`district1ID`, `district2ID`),
  CONSTRAINT `fk_DistrictDistance_1`
    FOREIGN KEY (`district1ID`)
    REFERENCES `ewebstore`.`District` (`districtID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DistrictDistance_2`
    FOREIGN KEY (`district2ID`)
    REFERENCES `ewebstore`.`District` (`districtID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`ProductCategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`ProductCategory` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`ProductCategory` (
  `productID` BIGINT NOT NULL,
  `categoryID` BIGINT NOT NULL,
  PRIMARY KEY (`productID`, `categoryID`),
  INDEX `fk_ProductCategory_2_idx` (`categoryID` ASC),
  CONSTRAINT `fk_ProductCategory_1`
    FOREIGN KEY (`productID`)
    REFERENCES `ewebstore`.`Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductCategory_2`
    FOREIGN KEY (`categoryID`)
    REFERENCES `ewebstore`.`Category` (`categoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewebstore`.`BranchInventoryTransfer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ewebstore`.`BranchInventoryTransfer` ;

CREATE TABLE IF NOT EXISTS `ewebstore`.`BranchInventoryTransfer` (
  `fromBranchID` BIGINT NOT NULL,
  `toBranchID` BIGINT NOT NULL,
  `productID` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  `inventoryTransferID` BIGINT NOT NULL AUTO_INCREMENT,
  `transferStatus` INT NULL,
  PRIMARY KEY (`inventoryTransferID`),
  INDEX `fk_BranchInventoryTransfer_2_idx` (`toBranchID` ASC),
  INDEX `fk_BranchInventoryTransfer_3_idx` (`productID` ASC),
  UNIQUE INDEX `fromBranchID_UNIQUE` (`fromBranchID` ASC),
  UNIQUE INDEX `toBranchID_UNIQUE` (`toBranchID` ASC),
  UNIQUE INDEX `productID_UNIQUE` (`productID` ASC),
  CONSTRAINT `fk_BranchInventoryTransfer_1`
    FOREIGN KEY (`fromBranchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BranchInventoryTransfer_2`
    FOREIGN KEY (`toBranchID`)
    REFERENCES `ewebstore`.`Branch` (`branchID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BranchInventoryTransfer_3`
    FOREIGN KEY (`productID`)
    REFERENCES `ewebstore`.`Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `ewebstore`.`District`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`District` (`districtID`, `districtName`) VALUES (14, 'Dhaka');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`Branch`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`Branch` (`branchID`, `branchName`, `branchLocation`, `branchDistrict`) VALUES (NULL, 'Dhaka Branch', 'Dhaka, Bangladesh', 14);

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`BranchManager`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`BranchManager` (`branchID`, `name`, `gender`, `email`, `address`, `dob`, `contactnumber`, `managerID`, `password`) VALUES (1, 'Jisan Mahmud', 1, 'a@a', 'Gazipur, Bangladesh', 'CURDATE()', '01679085859', NULL, 'a');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`Customer`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`Customer` (`customerID`, `name`, `dob`, `gender`, `email`, `address`, `contactNumber`, `registrationDate`, `premiumCustomer`, `totalTransaction`, `password`) VALUES (NULL, 'Jamshed', '2013-09-23', 1, 'qq@q', 'Uttara', '015', '2013-09-23', 1, 0, 'asd');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`Category`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (1, 'Appliances');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (2, 'Baby Products');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (3, 'Automotive');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (4, 'Books');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (5, 'Cell Phones');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (6, 'Clothings and Shoes');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (7, 'Computers');
INSERT INTO `ewebstore`.`Category` (`categoryID`, `categoryName`) VALUES (8, 'Electronics');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`OrderStatus`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`OrderStatus` (`orderStatusID`, `status`) VALUES (NULL, 'Unhandled');
INSERT INTO `ewebstore`.`OrderStatus` (`orderStatusID`, `status`) VALUES (NULL, 'Being Delivered');
INSERT INTO `ewebstore`.`OrderStatus` (`orderStatusID`, `status`) VALUES (NULL, 'Delivered');
INSERT INTO `ewebstore`.`OrderStatus` (`orderStatusID`, `status`) VALUES (NULL, 'Failed Delivery');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ewebstore`.`Order`
-- -----------------------------------------------------
START TRANSACTION;
USE `ewebstore`;
INSERT INTO `ewebstore`.`Order` (`orderID`, `customerID`, `orderDate`, `detailedDeliveryLocation`, `orderStatusID`, `branchID`, `associatedEmployee`, `totalOrderingCost`) VALUES (NULL, 4, '2013-09-23', 'dhaka', 5, 2, NULL, 10);

COMMIT;

