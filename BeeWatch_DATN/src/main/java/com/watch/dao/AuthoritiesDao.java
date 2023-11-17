package com.watch.dao;

import com.watch.entity.Accounts;
import com.watch.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthoritiesDao extends JpaRepository<Authorities, Long>{

	@Query("SELECT DISTINCT a FROM Authorities a WHERE a.account IN ?1")
	List<Authorities> authoritiesOf(List<Accounts> account);
	@Query("SELECT  a.account FROM Authorities a WHERE a.account.accountId =?1")
	Accounts findAcountAuthority(Long idAcc);
	
	@Query("SELECT  count(a) FROM Authorities a WHERE a.account.accountId =?1")
	int findAcountAuthorityCheck(Long idAcc);
}
