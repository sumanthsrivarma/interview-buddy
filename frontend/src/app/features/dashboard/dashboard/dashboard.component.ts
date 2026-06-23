import { Component, OnInit, inject } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { DashboardService } from '../../../core/services/dashboard.service';
import { TopicBreakdown, WeakArea, WeeklyStreak } from '../../../core/models/dashboard.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [BaseChartDirective, MatCardModule, MatListModule, MatIconModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  weakAreas: WeakArea[] = [];
  topicChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  streakChartData: ChartData<'bar'> = { labels: [], datasets: [] };

  topicChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    plugins: { legend: { position: 'top' } },
    scales: { x: { stacked: false }, y: { beginAtZero: true, ticks: { stepSize: 1 } } }
  };

  streakChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
  };

  ngOnInit(): void {
    forkJoin({
      summary: this.dashboardService.getSummary(),
      weak: this.dashboardService.getWeakAreas(),
      streak: this.dashboardService.getStreak()
    }).subscribe(({ summary, weak, streak }) => {
      this.weakAreas = weak;
      this.buildTopicChart(summary);
      this.buildStreakChart(streak);
    });
  }

  private buildTopicChart(data: TopicBreakdown[]): void {
    this.topicChartData = {
      labels: data.map(d => d.topic),
      datasets: [
        { label: 'Confident', data: data.map(d => d.confident), backgroundColor: '#4caf50' },
        { label: 'Needs Work', data: data.map(d => d.needsWork), backgroundColor: '#ff9800' },
        { label: 'Skipped', data: data.map(d => d.skipped), backgroundColor: '#9e9e9e' }
      ]
    };
  }

  private buildStreakChart(data: WeeklyStreak[]): void {
    this.streakChartData = {
      labels: data.map(d => d.week),
      datasets: [
        { label: 'Sessions', data: data.map(d => d.sessionCount), backgroundColor: '#1976d2' }
      ]
    };
  }
}
