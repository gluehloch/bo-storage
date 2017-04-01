select
    round from GameList as round
    left join fetch round.gameList game
    left join fetch game.tippList tipp
    left join fetch tipp.user u
    left join fetch game.homeTeam
    left join fetch game.guestTeam
    left join fetch game.group
where
    round.id = :roundId
    and u.id = :userId
