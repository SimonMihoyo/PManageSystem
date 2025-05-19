-- 创建数据库（如果不存在）
-- CREATE DATABASE IF NOT EXISTS PManageStore;
-- USE PManageStore;
-- 初始化数据库已移至代码中，避免循环依赖

-- 1. Type 表（账号类型表，无外键依赖）
CREATE TABLE IF NOT EXISTS Type (
    iTypeId INT PRIMARY KEY AUTO_INCREMENT,
    cTypeName CHAR(10) NOT NULL
);

-- 2. Supplier 表（供货商表，无外键依赖）
CREATE TABLE IF NOT EXISTS Supplier (
    iSupplierId INT PRIMARY KEY AUTO_INCREMENT,
    vSupplierName VARCHAR(50) NOT NULL,
    cContanctMan CHAR(10) NOT NULL,
    cContanctPhone CHAR(15) NOT NULL,
    vContanctAddress VARCHAR(100) NOT NULL,
    Description VARCHAR(100),
    cPostalCode CHAR(6) NOT NULL,
    SimplifiedCode CHAR(15) NOT NULL,
    BusinessScope VARCHAR(100) NOT NULL
);

-- 3. Manufacturer 表（生产单位表，无外键依赖）
CREATE TABLE IF NOT EXISTS Manufacturer (
    iFacturerId INT PRIMARY KEY AUTO_INCREMENT,
    vFactuerName VARCHAR(50) NOT NULL,
    cContanctMan CHAR(10) NOT NULL,
    cContanctPhone CHAR(15) NOT NULL,
    vContanctAddress VARCHAR(50) NOT NULL,
    Description VARCHAR(100),
    cPostalCode CHAR(6) NOT NULL,
    SimplifiedCode CHAR(15) NOT NULL,
    vBusinecssScope VARCHAR(100) NOT NULL
);

-- 4. Pharmacy 表（药房表，无外键依赖）
CREATE TABLE IF NOT EXISTS Pharmacy (
    iPharmacyId INT PRIMARY KEY AUTO_INCREMENT,
    vPharmacyName VARCHAR(50) NOT NULL,
    PharmacyDescription VARCHAR(255)
);

-- 5. MedicineGeneral 表（药品类别表，无外键依赖）
CREATE TABLE IF NOT EXISTS MedicineGeneral (
    iMedicineGeneralId INT PRIMARY KEY AUTO_INCREMENT,
    iMedicineGeneraName VARCHAR(50), -- 调整为 VARCHAR
    MedicineDescription VARCHAR(255)
);

-- 6. Medicine 表（药品表，依赖 Manufacturer 和 MedicineGeneral）
CREATE TABLE IF NOT EXISTS Medicine (
    iMedicineId INT PRIMARY KEY AUTO_INCREMENT,
    MedicineName VARCHAR(30) NOT NULL,
    MedicineDescription VARCHAR(255),
    iFacturerId INT,
    iMedicineGeneralId INT,
    mRetailPrice decimal CHECK (mRetailPrice > 0),
    MedicineStandard VARCHAR(20) NOT NULL,
    MedicineDosage VARCHAR(20) NOT NULL,
    FOREIGN KEY (iFacturerId) REFERENCES Manufacturer(iFacturerId),
    FOREIGN KEY (iMedicineGeneralId) REFERENCES MedicineGeneral(iMedicineGeneralId)
);

-- 7. User 表（用户表，依赖 Type）
CREATE TABLE IF NOT EXISTS User (
    iUserId INT PRIMARY KEY AUTO_INCREMENT,
    vUserName VARCHAR(20) NOT NULL,
    vUserPass VARCHAR(20) NOT NULL,
    vUserRealName VARCHAR(20) NOT NULL,
    iTypeId INT,
    FOREIGN KEY (iTypeId) REFERENCES Type(iTypeId)
);

-- 8. InputList 表（入库单表，无外键依赖）
CREATE TABLE IF NOT EXISTS InputList (
    iInputListId INT PRIMARY KEY AUTO_INCREMENT,
    dInputDate DATETIME NOT NULL
);

-- 9. UllageList 表（缺货统计单表，依赖 Medicine）
CREATE TABLE IF NOT EXISTS UllageList (
    iUllageListId INT PRIMARY KEY AUTO_INCREMENT,
    iMedicineId INT,
    iPresentQuantity INT,
    FOREIGN KEY (iMedicineId) REFERENCES Medicine(iMedicineId)
);

-- 10. SupplierMedicine 表（供货商药品表，依赖 Supplier 和 Medicine）
CREATE TABLE IF NOT EXISTS SupplierMedicine (
    iSupplierId INT,
    iMedicineId INT,
    iMinimunStorage INT CHECK (iMinimunStorage > 0),
    mInputPrice decimal CHECK (mInputPrice > 0),
    iPresentQuantity INT CHECK (iPresentQuantity > 0),
    PRIMARY KEY (iSupplierId, iMedicineId),
    FOREIGN KEY (iSupplierId) REFERENCES Supplier(iSupplierId),
    FOREIGN KEY (iMedicineId) REFERENCES Medicine(iMedicineId)
);

-- 11. OutputList 表（出库单表，依赖 Medicine 和 Pharmacy）
CREATE TABLE IF NOT EXISTS OutputList (
    iOutputId INT,
    iMedicineId INT,
    iOutputQuantity INT CHECK (iOutputQuantity > 0),
    iPharmacyId INT,
    PRIMARY KEY (iOutputId, iMedicineId),
    FOREIGN KEY (iMedicineId) REFERENCES Medicine(iMedicineId),
    FOREIGN KEY (iPharmacyId) REFERENCES Pharmacy(iPharmacyId)
);

-- 12. Application 表（申购单表，依赖 Medicine）
CREATE TABLE IF NOT EXISTS Application (
    ApplicationId INT PRIMARY KEY AUTO_INCREMENT,
    iMedicineId INT,
    iQuantityOfApplication INT CHECK (iQuantityOfApplication > 0),
    dDateOfApplication DATE NOT NULL,
    bStateOfApplication BOOLEAN NOT NULL,
    iPresentQuantity INT NOT NULL,
    FOREIGN KEY (iMedicineId) REFERENCES Medicine(iMedicineId)
);

-- 13. InputListDetail 表（入库单详情表，依赖 InputList、Medicine、Manufacturer 和 Supplier）
CREATE TABLE IF NOT EXISTS InputListDetail (
    iInputListId INT,
    iMedicineId INT,
    iInputQuantity INT CHECK (iInputQuantity > 0),
    dDateOfExpiry DATETIME NOT NULL,
    dDateOfProduction DATETIME NOT NULL,
    iFacturerId INT,
    iSupplierId INT,
    PRIMARY KEY (iInputListId, iMedicineId),
    FOREIGN KEY (iInputListId) REFERENCES InputList(iInputListId),
    FOREIGN KEY (iMedicineId) REFERENCES Medicine(iMedicineId),
    FOREIGN KEY (iFacturerId) REFERENCES Manufacturer(iFacturerId),
    FOREIGN KEY (iSupplierId) REFERENCES Supplier(iSupplierId)
);

-- 往 Type 表插入记录
INSERT INTO Type (cTypeName)
VALUES
    ('Admin'),
    ('User'),
    ('Auditor'),
    ('Guest'),
    ('Demo');