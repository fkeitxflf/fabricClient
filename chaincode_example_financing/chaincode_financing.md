信用贷款管理

简介

该智能合约实现一个简单的担保融资流程：

核心企业为融资企业向区块链平台提交融资申请。
初始化核心企业，初始化融资企业。
平台为融资企业，针对不同银行发行一定量数字货币。
融资企业向核心企业转移一定量数字货币来担保获得贷款。
融资企业归还贷款后，核心企业归还货币给融资企业用于下一次贷款。
数字货币发行与管理

数据结构设计
// 融资申请
type Financing struct {
	ID					int
	CoreCompanyID       string	// 核心企业ID
  FinanceCompanyID 	string  // 融资企业ID
  BankID  			string
	Amount 				int
}
