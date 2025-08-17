package woojooin.planit.domain.member.mapper;

import woojooin.planit.domain.member.domain.MemberProduct;

import java.util.List;

public interface MemberProductMapper {
    void insert(MemberProduct memberProduct);
    void insertAll(List<MemberProduct> memberProducts);
    List<MemberProduct> select(Long memberId);
}
